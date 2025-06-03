package com.balybus.galaxy.chat.domain.tblChatRoom;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TblChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Comment(value="로그인 구분자 중 더 작은 값")
    @JoinColumn(name="least_seq", nullable = false)
    private TblUser userA;     //로그인 구분자 중 더 작은 값

    @ManyToOne
    @Comment(value="로그인 구분자 중 더 큰 값")
    @JoinColumn(name="greatest_seq", nullable = false)
    private TblUser userB;   //로그인 구분자 중 더 큰 값

    @ManyToOne
    @Comment(value="환자 구분자")
    @JoinColumn(name="patient_log_seq", nullable = false)
    private TblPatientLog patientLog;   //환자 구분자
}
