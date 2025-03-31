package com.balybus.galaxy.patient.domain.tblPatient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblPatientRepository extends JpaRepository<TblPatient, Long> {

    Optional<TblPatient> findByName(String name);
    Page<TblPatient> findByManagerId(Long managerSeq, Pageable page);
}
