package com.balybus.galaxy.patient.repository;

import com.balybus.galaxy.patient.domain.TblPatientTime;
import com.balybus.galaxy.patient.domain.TblPatientTimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblPatientTimeRepository extends JpaRepository<TblPatientTime, Long> {

}
