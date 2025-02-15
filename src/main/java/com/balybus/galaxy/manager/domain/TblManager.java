package com.balybus.galaxy.manager.domain;

import com.balybus.galaxy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class TblManager extends BaseEntity {

    @Id
    @Column(name = "cm_seq")
    @Comment("관리자 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 관리자 구분자 (CM_SEQ)

    @Column(name = "user_seq", nullable = false)
    @Comment("유저 구분자")
    private Long userSeq;  // 로그인 구분자 (USER_SEQ)

    @Column(name = "center_seq", nullable = false)
    @Comment("센터 구분자")
    private Long centerSeq;  // 센터 구분자 (CENTER_SEQ)

    @Column(name = "cm_position", length = 10)
    @Comment("직책")
    private String position;  // 직책 (CM_POSITION)

    @Column(name = "cm_name", length = 10, nullable = false)
    @Comment("직원명")
    private String name;  // 직원명 (CM_NAME)

    @Column(name = "cm_password", nullable = false)
    @Comment("비밀번호")
    private String password;  // 비밀번호 (CM_PASSWORD)

    @Column(name = "create_datetime", nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;  // 생성일시 (CREATE_DATETIME)

    @Column(name = "update_datetime", nullable = false)
    @Comment("수정일시")
    private LocalDateTime updatedAt;  // 수정일시 (UPDATE_DATETIME)

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();  // 생성일시 자동 설정
        updatedAt = LocalDateTime.now();  // 수정일시 자동 설정
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();  // 수정일시 자동 설정
    }
}
