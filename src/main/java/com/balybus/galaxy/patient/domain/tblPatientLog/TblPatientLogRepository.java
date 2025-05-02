package com.balybus.galaxy.patient.domain.tblPatientLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TblPatientLogRepository extends JpaRepository<TblPatientLog, Long> {
    Page<TblPatientLog> findByManagerId(Long managerSeq, Pageable page);
    Page<TblPatientLog> findByPatientIdAndManagerId(Long patientSeq, Long managerSeq, Pageable page);
    List<TblPatientLog> findAllByManagerId(Long managerSeq);
}
