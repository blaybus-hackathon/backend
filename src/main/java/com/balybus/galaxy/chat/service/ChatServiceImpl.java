package com.balybus.galaxy.chat.service;

import com.balybus.galaxy.chat.domain.tblChatMsg.TblChatMsg;
import com.balybus.galaxy.chat.domain.tblChatMsg.TblChatMsgRepository;
import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoom;
import com.balybus.galaxy.chat.domain.tblChatRoom.TblChatRoomRepository;
import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//        Optional<TblChatRoom> chatRoomOpt = chatRoomRepository.findByUserA_Id(userA.getId(), userB.getId(), patientLogEntity.getId());
        TblChatRoom chatRoom = chatRoomRepository.save(TblChatRoom.builder()
                .userA(userA)
                .userB(userB)
                .patientLog(patientLogOpt.get())
                .build());

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
     * @param userEmail String
     * @return List<ChatMsgResponseDto.FindList>
     */
    @Override
    public List<ChatMsgResponseDto.FindList> findList(String userEmail) {
        Optional<TblUser> userOpt = memberRepository.findByEmail(userEmail);
        if(userOpt.isEmpty()) throw new BadRequestException(ExceptionCode.MEMBER_NOT_FOUND);

//        List<Object[]> findList = chatRoomRepository.findChatGroupList(userOpt.get().getId());
        return null;
    }

    @Override
    public List<ChatMsgResponseDto.FindChatDetail> findChatDetail(ChatMsgRequestDto.FindChatDetail dto) {
//        chatMsgRepository.findBySender_IdAndReceiver_IdAndPatientLog_Id(senderId, receiverId, patientLogId);
        return null;
    }
}
