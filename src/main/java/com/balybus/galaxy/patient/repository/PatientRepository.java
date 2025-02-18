package com.balybus.galaxy.patient.repository;

import com.balybus.galaxy.patient.domain.TblPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<TblPatient, Long> {
}
