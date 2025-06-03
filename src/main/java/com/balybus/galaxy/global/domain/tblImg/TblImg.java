package com.balybus.galaxy.global.domain.tblImg;

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
public class TblImg extends BaseEntity {
    @Id
    @Column(name="img_seq")
    @Comment(value="관리자 구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 구분자

    @Comment(value="저장된 파일명")
    @Column(length = 225, nullable = false)
    private String imgUuid;         // 저장된 파일명

    @Comment(value="원본 파일명")
    @Column(length = 225, nullable = false)
    private String imgOriginName;   // 원본 파일명

}
