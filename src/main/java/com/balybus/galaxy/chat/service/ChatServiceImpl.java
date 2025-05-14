package com.balybus.galaxy.chat.service;

import com.balybus.galaxy.chat.domain.tblChatMsg.TblChatMsg;
import com.balybus.galaxy.chat.domain.tblChatMsg.TblChatMsgRepository;
import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoom;
import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoomRepository;
import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
import com.balybus.galaxy.chat.dto.ChatRoomResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.global.utils.file.service.FileService;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {
    private final FileService fileService;

    private final TblChatRoomRepository chatRoomRepository;
    private final TblChatMsgRepository chatMsgRepository;
    private final MemberRepository memberRepository;
    private final TblCenterManagerRepository centerManagerRepository;
    private final HelperRepository helperRepository;
    private final TblPatientLogRepository patientLogRepository;

    /**
     * 전송하는 채팅 DB 저장
     * @param dto ChatMsgRequestDto.SendPrivateMessage
     * @param userEmail String
     * @return ChatMsgResponseDto.SendPrivateMessage
     */
    @Transactional
    public ChatMsgResponseDto.SendPrivateMessage saveMessage(ChatMsgRequestDto.SendPrivateMessage dto, String userEmail) {
        //1. sender 조회
        Optional<TblUser> senderOpt = memberRepository.findByEmail(userEmail);
        if(senderOpt.isEmpty() || !senderOpt.get().getId().equals(dto.getSenderId()))
            throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_SENDER);

        //2. receiver 조회
        Optional<TblUser> receiverOpt = memberRepository.findById(dto.getReceiverId());
        if(receiverOpt.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_RECEIVER);

        //3. patient 조회
        Optional<TblPatientLog> patientLogOpt = patientLogRepository.findById(dto.getPatientLogId());
        if(patientLogOpt.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_PATIENT_LOG);

        //3. 채팅방 엔티티 생성 & 저장
        TblUser senderEntity = senderOpt.get();
        TblUser receiverEntity = receiverOpt.get();
        TblPatientLog patientLogEntity = patientLogOpt.get();

        TblUser userA = senderEntity.getId() < receiverEntity.getId() ? senderEntity : receiverEntity;
        TblUser userB = senderEntity.getId() >= receiverEntity.getId() ? senderEntity : receiverEntity;

        Optional<TblChatRoom> chatRoomOpt = chatRoomRepository.findByUserA_IdAndUserB_IdAndPatientLog_Id(userA.getId(), userB.getId(), patientLogEntity.getId());
        TblChatRoom chatRoom = chatRoomOpt.orElseGet(() ->
                chatRoomRepository.save(TblChatRoom.builder()
                        .userA(userA)
                        .userB(userB)
                        .patientLog(patientLogOpt.get())
                        .build()));

        //4. 채팅 내역 엔티티 생성 & 저장
        chatMsgRepository.save(TblChatMsg.builder()
                .chatRoom(chatRoom)
                .sender(senderEntity)
                .content(dto.getContent())
                .readYn(false)
                .build());

        //5. dto 반환
        String senderName = "알 수 없는 사용자";
        RoleType senderAuth = senderEntity.getUserAuth();
        if(RoleType.MANAGER.equals(senderAuth)){
            // 관리자 권한인 경우,
            Optional<TblCenterManager> manager = centerManagerRepository.findByMember_Id(senderEntity.getId());
            if(manager.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_SENDER);
            senderName = manager.get().getCmName();
        } else if(RoleType.MEMBER.equals(senderAuth)){
            // 요양보호사인 경우,
            Optional<TblHelper> helper = helperRepository.findByUserId(senderEntity.getId());
            if(helper.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_SENDER);
            senderName = helper.get().getName();
        }

        return ChatMsgResponseDto.SendPrivateMessage.builder()
                .senderId(dto.getSenderId())
                .senderName(senderName)
                .receiverId(dto.getReceiverId())
                .receiverMail(receiverEntity.getEmail())
                .patientLogId(dto.getPatientLogId())
                .content(dto.getContent())
                .build();
    }

    /**
     * 채팅방 리스트 조회
     *
     * @param userEmail String
     * @return List<ChatRoomResponseDto.FindList>
     */
    @Override
    public List<ChatRoomResponseDto.FindList> findList(String userEmail) {
        // 1. 로그인 사용자 정보 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(userEmail);
        if(userOpt.isEmpty()) throw new BadRequestException(ExceptionCode.MEMBER_NOT_FOUND);
        TblUser userEntity = userOpt.get();

        // 2. 로그인 사용자가 참가한 채팅방 리스트 조회
        List<Object[]> objectList = chatRoomRepository.findObjectList(userEntity.getId());

        // 3. entity -> dto 전환
        List<ChatRoomResponseDto.FindList> result = new ArrayList<>();
        for(Object[] entity : objectList) {
            //3-1. 채팅 상대 정보 조회
            Long partnerId = (Long) entity[1];
            Optional<TblUser> partnerUserOpt = memberRepository.findById(partnerId);
            if(partnerUserOpt.isEmpty()) continue;

            //3-2. 채팅 상대 이미지 주소 조회
            TblUser partnerUser = partnerUserOpt.get();
            String partnerImgStr = null;
            try{
                partnerImgStr = getPartnerImg(partnerUser);
            } catch(BadRequestException e){
                continue;
            }

            //3-3. 마지막 대화 및 안읽은 대화 개수 카운트
            Long chatRoomId = (Long) entity[0];
            Optional<TblChatMsg> chatMsgOpt = chatMsgRepository.findTop1ByChatRoom_IdOrderByIdDesc(chatRoomId);

            // 3-3. dto 전환 및 리스트 추가
            result.add(ChatRoomResponseDto.FindList.builder()
                    .chatRoomId(chatRoomId)
                    .partnerId(partnerId)
                    .partnerName((String) entity[2])
                    .partnerImgAddress(partnerImgStr)
                    .lastChatContent(chatMsgOpt.map(TblChatMsg::getContent).orElse(null))
                    .lastChatSendTime(chatMsgOpt.map(TblChatMsg::getCreateDatetime).orElse(null))
                    .notReadCnt(chatMsgRepository.countByChatRoomIdAndSenderIdAndReadYn(chatRoomId, partnerUser.getId(), false))
                    .patientLogId((Long) entity[3])
                    .patientLogName((String) entity[4])
                    .build());
        }
        return result;
    }

    @Override
    @Transactional
    public ChatMsgResponseDto.FindChatDetail findChatDetail(ChatMsgRequestDto.FindChatDetail dto, String userEmail) {
        //1. 로그인 사용자 조회
        Optional<TblUser> userOpt = memberRepository.findByEmail(userEmail);
        if(userOpt.isEmpty())
            throw new BadRequestException(ExceptionCode.MEMBER_NOT_FOUND);
        TblUser userEntity = userOpt.get();

        //2. 채팅방 조회
        Optional<TblChatRoom> chatRoomOpt = chatRoomRepository.findByIdAndOrUser(dto.getChatRoomId(), userEntity.getId());
        if(chatRoomOpt.isEmpty())
            throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_CHAT_ROOM);
        TblChatRoom chatRoomEntity = chatRoomOpt.get();

        //3. 채팅 상대 조회
        Optional<TblUser> partnerUserOpt = memberRepository.findById(
                userEntity.getId().equals(chatRoomEntity.getUserA().getId())
                        ? chatRoomEntity.getUserB().getId()
                        : chatRoomEntity.getUserA().getId());
        if(partnerUserOpt.isEmpty())
            throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
        TblUser partnerUser = partnerUserOpt.get();
        String partnerImgStr = getPartnerImg(partnerUser);

        //4. 상대 전송 채팅 읽음 처리
        setRead(chatRoomEntity.getId(), partnerUser.getId());

        //5. 채팅 내역 조회
        Pageable page = PageRequest.of(dto.getPageNo(), 30, Sort.by(Sort.Direction.DESC, "id"));
        Page<TblChatMsg> chatListPage = chatMsgRepository.findByChatRoom_Id(dto.getChatRoomId(), page);

        List<TblChatMsg> chatList = chatListPage.getContent();
        List<ChatMsgResponseDto.FindChatDetailList> chatListDto = new ArrayList<>();
        for(TblChatMsg entity : chatList) {
            chatListDto.add(ChatMsgResponseDto.FindChatDetailList.builder()
                    .senderYn(userEntity.equals(entity.getSender()))
                    .content(entity.getContent())
                    .sendTime(entity.getCreateDatetime())
                    .readYn(entity.isReadYn())
                    .build());
        }

        return ChatMsgResponseDto.FindChatDetail.builder()
                .hasNext(chatListPage.hasNext())
                .partnerImgAddress(partnerImgStr)
                .list(chatListDto)
                .build();
    }

    /**
     * 대화 상대 이미지 조회
     * @param partnerUser TblUser:대화상대 로그인 entity
     * @return String:이미지 주소
     */
    private String getPartnerImg(TblUser partnerUser){
        String partnerImgStr = null;
        if(RoleType.MEMBER.equals(partnerUser.getUserAuth())){ // 요양보호사
            Optional<TblHelper> helperOpt = helperRepository.findByUserId(partnerUser.getId());
            if(helperOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
            partnerImgStr = helperOpt.get().getImg() == null ? null : fileService.getOneImgUrl(helperOpt.get().getImg().getId());
        } else if(RoleType.MANAGER.equals(partnerUser.getUserAuth())){ // 관리자
            Optional<TblCenterManager> centerManagerOpt = centerManagerRepository.findByMember_Id(partnerUser.getId());
            if(centerManagerOpt.isEmpty()) throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
            partnerImgStr = centerManagerOpt.get().getImg() == null ? null : fileService.getOneImgUrl(centerManagerOpt.get().getImg().getId());
        } else throw new BadRequestException(ExceptionCode.INVALID_REQUEST); // 이외 대화 불가

        return partnerImgStr;
    }

    private void setRead(Long chatRoomId, Long partnerUserId){
        // 로그인 사용자가 참여중인 채팅방이라는 전제로 아래 로직 실행.
        //1. 상대방이 송신한 채팅 중, 읽지 않은 메시지 전체 조회
        List<TblChatMsg> notReadChatList = chatMsgRepository.findByChatRoomIdAndSenderIdAndReadYn(chatRoomId, partnerUserId, false);

        //2. 해당 데이터 전부 읽음 처리
        notReadChatList.forEach(TblChatMsg::chRead);
    }
}
