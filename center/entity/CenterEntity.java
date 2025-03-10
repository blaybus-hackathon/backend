package com.balybus.galaxy.center.entity;

import com.balybus.galaxy.centermanager.entity.CenterManagerEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CENTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)  // 자동 날짜 기록을 위해 추가
public class CenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK

    private String issueCode;   // 발급코드
    private String name;        // 센터명
    private String address;     // 센터 주소
    private String description; // 한줄소개
    private String contactNumber; // 연락처
    private String colorCode;   // 색상코드

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate; // 생성일

    private Integer createdBy;  // 생성자 ID

    @LastModifiedDate
    private LocalDateTime updatedDate; // 수정일

    private Integer updatedBy;  // 수정자 ID

    public void setCenterManager(CenterManagerEntity centerManager) {
    }
}