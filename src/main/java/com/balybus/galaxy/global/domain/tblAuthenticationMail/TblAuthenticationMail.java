package com.balybus.galaxy.global.domain.tblAuthenticationMail;

import com.balybus.galaxy.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TblAuthenticationMail extends BaseEntity {
    @Id
    @Column(name="am_seq")
    @Comment(value="인증코드 메일 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    //구분자
    private String email;               //이메일
    private String code;                //인증코드
    private boolean certificationYn;    //인증여부

    /* ==================================================
     * UPDATE
     * ================================================== */
    public void updateCode(String code) {
        this.code = code;
        this.certificationYn = false;
    }
    public void approve() {
        this.certificationYn = true;
    }
}
