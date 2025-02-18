package com.balybus.galaxy.patient.dto;

import com.balybus.galaxy.patient.domain.TblPatient;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Long id;
    private Long managerSeq;
    private Long longTermCareGradeSeq;
    private Long guardianInfoSeq;
    private String name;
    private String birthDate;
    private String gender;  // gender 필드 타입을 String으로 수정
    private Double weight;
    private String diseases;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PatientDto fromEntity(TblPatient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .managerSeq(patient.getManagerSeq())
                .longTermCareGradeSeq(patient.getLongTermCareGradeSeq())
                .guardianInfoSeq(patient.getGuardianInfoSeq())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .weight(patient.getWeight())
                .diseases(patient.getDiseases())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }

    public TblPatient toEntity() {
        return TblPatient.builder()
                .managerSeq(this.managerSeq)
                .longTermCareGradeSeq(this.longTermCareGradeSeq)
                .guardianInfoSeq(this.guardianInfoSeq)
                .name(this.name)
                .birthDate(this.birthDate)
                .gender(this.gender)
                .weight(this.weight)
                .diseases(this.diseases)
                .build();
    }
}
