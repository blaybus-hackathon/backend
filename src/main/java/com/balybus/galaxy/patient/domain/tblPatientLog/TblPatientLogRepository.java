package com.balybus.galaxy.patient.domain.tblPatientLog;

import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblPatientLogRepository extends JpaRepository<TblPatientLog, Long> {
    List<TblPatientLog> findByManagerAndMatchingStatus(TblCenterManager manager, int matchingStatus);
}