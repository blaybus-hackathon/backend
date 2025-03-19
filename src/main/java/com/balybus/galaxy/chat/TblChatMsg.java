package com.balybus.galaxy.chat;

import com.balybus.galaxy.domain.BaseEntity;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
public class TblChatMsg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Comment(value="송신자 구분자")
    @JoinColumn(name="sender_seq", nullable = false)
    private TblUser sender;     //송신자 구분자

    @ManyToOne
    @Comment(value="수신자 구분자")
    @JoinColumn(name="receiver_seq", nullable = false)
    private TblUser receiver;   //수신자 구분자

    @ManyToOne
    @Comment(value="환자 구분자")
    @JoinColumn(name="patient_log_seq", nullable = false)
    private TblPatientLog patientLog;   //환자 구분자

    private String content;
}
