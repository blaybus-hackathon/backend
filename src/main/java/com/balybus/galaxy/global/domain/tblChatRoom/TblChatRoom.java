package com.balybus.galaxy.global.domain.tblChatRoom;

import com.balybus.galaxy.global.domain.BaseEntity;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.global.domain.tblPatientLog.TblPatientLog;
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

    @Comment(value="A 사용자 방 나가기 여부")
    private boolean outUserA;   //A 사용자 나가기 여부

    @ManyToOne
    @Comment(value="로그인 구분자 중 더 큰 값")
    @JoinColumn(name="greatest_seq", nullable = false)
    private TblUser userB;   //로그인 구분자 중 더 큰 값

    @Comment(value="B 사용자 방 나가기 여부")
    private boolean outUserB;   //B 사용자 나가기 여부

    @ManyToOne
    @Comment(value="환자 구분자")
    @JoinColumn(name="patient_log_seq", nullable = false)
    private TblPatientLog patientLog;   //환자 구분자

    /* ==================================================
     * UPDATE
     * ================================================== */

    /**
     * 사용자 채팅방 나가기 처리
     * @param aYn boolean : A 사용자 여부 (true = A / false = B )
     */
    public void outUser(boolean aYn){
       if(aYn)
           this.outUserA = true;
       else
           this.outUserB = true;
    }

}
