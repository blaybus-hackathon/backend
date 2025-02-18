package com.balybus.galaxy.notice.domain;

import com.balybus.galaxy.notice.dto.NoticeDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "tbl_notice_patient")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TblNoticePatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_seq")
    @Comment("어르신 구분자")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Comment("어르신 이름")
    private String name;

    @Column(name = "birth_date", nullable = false)
    @Comment("생년월일")
    private String birthDate;

    @Column(name = "gender", length = 10)
    @Comment("성별")
    private String gender;

    @Column(name = "long_term_care_grade_seq", nullable = false)
    @Comment("어르신 장기요양등급 구분자")
    private Long longTermCareGradeSeq;

    @Column(name = "af_seq", nullable = false)
    @Comment("시도 구분자 (주소)")
    private String afSeq;

    @Column(name = "as_seq", nullable = false)
    @Comment("시군구 구분자 (주소)")
    private String asSeq;

    @Column(name = "at_seq", nullable = false)
    @Comment("읍면동 구분자 (주소)")
    private String atSeq;

    /* 필드 업데이트 메서드 추가 */
    public void update(NoticeDto noticeDto) {
        this.name = noticeDto.getName();
        this.birthDate = noticeDto.getBirthDate();
        this.gender = noticeDto.getGender();
        this.longTermCareGradeSeq = noticeDto.getLongTermCareGradeSeq();
        this.afSeq = noticeDto.getAfSeq();
        this.asSeq = noticeDto.getAsSeq();
        this.atSeq = noticeDto.getAtSeq();
    }
}
