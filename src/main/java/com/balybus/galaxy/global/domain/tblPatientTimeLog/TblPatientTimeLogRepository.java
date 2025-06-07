package com.balybus.galaxy.global.domain.tblPatientTimeLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblPatientTimeLogRepository extends JpaRepository<TblPatientTimeLog, Long> {
    List<TblPatientTimeLog> findByPatientLog_Id(Long patientLogSeq);
}
