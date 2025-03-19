package com.balybus.galaxy.chat;

import com.balybus.galaxy.chat.dto.ChatMsgRequestDto;
import com.balybus.galaxy.chat.dto.ChatMsgResponseDto;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.domain.tblCenterManager.TblCenterManagerRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import com.balybus.galaxy.global.exception.ExceptionCode;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.repositoryImpl.HelperRepository;
import com.balybus.galaxy.login.domain.type.RoleType;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {
    private final TblChatMsgRepository chatMsgRepository;
    private final MemberRepository memberRepository;
    private final TblCenterManagerRepository centerManagerRepository;
    private final HelperRepository helperRepository;
    private final TblPatientLogRepository patientLogRepository;

    // 전송하는 채팅 DB 저장
    @Transactional
    public ChatMsgResponseDto saveMessage(ChatMsgRequestDto dto, String userEmail) {
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

        //3. 엔티티 생성 & 저장
        TblUser senderEntity = senderOpt.get();
        chatMsgRepository.save(TblChatMsg.builder()
                .sender(senderEntity)
                .receiver(receiverOpt.get())
                .patientLog(patientLogOpt.get())
                .content(dto.getContent())
                .build());

        //4. dto 반환
        String senderName = "알 수 없는 사용자";
        RoleType senderAuth = senderEntity.getUserAuth();
        if(RoleType.MANAGER.equals(senderAuth)){
            // 관리자 권한인 경우,
            Optional<TblCenterManager> manager = centerManagerRepository.findByMember_Id(senderEntity.getId());
            if(manager.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_SENDER);
            senderName = manager.get().getCmName();
        } else if(RoleType.MEMBER.equals(senderAuth)){
            // 요양보호사인 경우,
            Optional<TblHelper> helper = helperRepository.findByUserId(senderEntity.getId());;
            if(helper.isEmpty()) throw new BadRequestException(ExceptionCode.WS_NOT_FOUND_SENDER);
            senderName = helper.get().getName();
        }

        return ChatMsgResponseDto.builder()
                .senderId(dto.getSenderId())
                .senderName(senderName)
                .receiverId(dto.getReceiverId())
                .patientLogId(dto.getPatientLogId())
                .content(dto.getContent())
                .build();
    }

    public List<TblChatMsg> getChatHistory(Long senderId, Long receiverId, Long patientLogId) {
        return chatMsgRepository.findBySender_IdAndReceiver_IdAndPatientLog_Id(senderId, receiverId, patientLogId);
    }
}
