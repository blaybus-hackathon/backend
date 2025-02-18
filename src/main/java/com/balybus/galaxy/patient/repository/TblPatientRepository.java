package com.balybus.galaxy.patient.repository;

import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import com.balybus.galaxy.patient.domain.TblPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblPatientRepository extends JpaRepository<TblPatient, Long> {
    List<TblPatient> findAllByManager(TblCenterManager manager);
}
