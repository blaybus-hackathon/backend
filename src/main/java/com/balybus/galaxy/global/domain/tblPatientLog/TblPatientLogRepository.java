package com.balybus.galaxy.global.domain.tblPatientLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblPatientLogRepository extends JpaRepository<TblPatientLog, Long> {
    Page<TblPatientLog> findByManagerId(Long managerSeq, Pageable page);
    Page<TblPatientLog> findByPatientIdAndManagerId(Long patientSeq, Long managerSeq, Pageable page);
    List<TblPatientLog> findAllByManagerId(Long managerSeq);
}
