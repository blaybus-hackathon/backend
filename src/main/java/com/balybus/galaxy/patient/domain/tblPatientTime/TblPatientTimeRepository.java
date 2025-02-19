package com.balybus.galaxy.patient.domain.tblPatientTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblPatientTimeRepository extends JpaRepository<TblPatientTime, Long> {
    List<TblPatientTime> findByPatient_Id(Long patientSeq);
}
