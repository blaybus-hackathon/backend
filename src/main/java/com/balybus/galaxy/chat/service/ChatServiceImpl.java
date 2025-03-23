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
            // 3-1. dto 전환 및 리스트 추가
            result.add(ChatRoomResponseDto.FindList.builder()
                    .chatRoomId((Long) entity[0])
                    .partnerId((Long) entity[1])
                    .partnerName((String) entity[2])
                    .patientLogId((Long) entity[3])
                    .patientLogName((String) entity[4])
                    .build());
        }
        return result;
    }

    @Override
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

        //3. 채팅 내역 조회
        Pageable page = PageRequest.of(dto.getPageNo(), 30, Sort.by(Sort.Direction.DESC, "id"));
        Page<TblChatMsg> chatListPage = chatMsgRepository.findByChatRoom_Id(dto.getChatRoomId(), page);

        List<TblChatMsg> chatList = chatListPage.getContent();
        List<ChatMsgResponseDto.FindChatDetailList> chatListDto = new ArrayList<>();
        for(TblChatMsg entity : chatList) {
            chatListDto.add(ChatMsgResponseDto.FindChatDetailList.builder()
                    .senderYn(userEntity.equals(entity.getSender()))
                    .content(entity.getContent())
                    .sendTime(entity.getCreateDatetime())
                    .build());
        }

        return ChatMsgResponseDto.FindChatDetail.builder()
                .hasNext(chatListPage.hasNext())
                .list(chatListDto)
                .build();
    }
}
