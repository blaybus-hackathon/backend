package com.balybus.galaxy.patient.domain.tblPatientTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblPatientTimeRepository extends JpaRepository<TblPatientTime, Long> {

}
