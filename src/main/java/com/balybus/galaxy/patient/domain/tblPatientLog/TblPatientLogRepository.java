package com.balybus.galaxy.patient.domain.tblPatientLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TblPatientLogRepository extends JpaRepository<TblPatientLog, Long> {
    Page<TblPatientLog> findByManagerId(Long managerSeq, Pageable page);
    Page<TblPatientLog> findByPatientIdAndManagerId(Long patientSeq, Long managerSeq, Pageable page);
    List<TblPatientLog> findAllByManagerId(Long managerSeq);

    @Query("SELECT pl FROM TblPatientLog pl JOIN FETCH pl.patient WHERE pl.manager.id = :managerId")
    List<TblPatientLog> findAllByManagerIdWithPatient(@Param("managerId") Long managerId);
}
