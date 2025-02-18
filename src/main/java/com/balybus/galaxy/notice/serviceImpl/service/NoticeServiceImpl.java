package com.balybus.galaxy.notice.serviceImpl.service;

import com.balybus.galaxy.notice.domain.TblNoticePatient;
import com.balybus.galaxy.notice.dto.NoticeDto;
import com.balybus.galaxy.notice.reporitory.NoticeRepository;
import com.balybus.galaxy.notice.serviceImpl.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    /* 공지사항 등록 */
    @Override
    public NoticeDto saveNotice(NoticeDto noticeDto) {
        TblNoticePatient patient = TblNoticePatient.builder()
                .name(noticeDto.getName())
                .birthDate(noticeDto.getBirthDate())
                .gender(noticeDto.getGender())
                .longTermCareGradeSeq(noticeDto.getLongTermCareGradeSeq())
                .afSeq(noticeDto.getAfSeq())
                .asSeq(noticeDto.getAsSeq())
                .atSeq(noticeDto.getAtSeq())
                .build();

        TblNoticePatient savedPatient = noticeRepository.save(patient);

        return convertToDto(savedPatient);
    }

    /* 공지사항 수정 */
    @Override
    public NoticeDto updateNotice(NoticeDto noticeDto) {
        TblNoticePatient patient = noticeRepository.findById(noticeDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 없습니다."));

        patient.update(noticeDto);
        TblNoticePatient updatedPatient = noticeRepository.save(patient);

        return convertToDto(updatedPatient);
    }

    /* 엔티티 → DTO 변환 */
    private NoticeDto convertToDto(TblNoticePatient patient) {
        NoticeDto dto = new NoticeDto();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setBirthDate(patient.getBirthDate());
        dto.setGender(patient.getGender());
        dto.setLongTermCareGradeSeq(patient.getLongTermCareGradeSeq());
        dto.setAfSeq(patient.getAfSeq());
        dto.setAsSeq(patient.getAsSeq());
        dto.setAtSeq(patient.getAtSeq());
        return dto;
    }
}
