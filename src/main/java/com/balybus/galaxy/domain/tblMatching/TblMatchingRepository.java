package com.balybus.galaxy.domain.tblMatching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblMatchingRepository extends JpaRepository<TblMatching, Long> {
    List<TblMatching> findByPatientLog_id(Long plSeq);
    Optional<TblMatching> findByPatientLog_idAndHelper_id(Long plSeq, Long helperSeq);
    List<TblMatching> findByPatientLog_idAndMatchState(Long plSeq, MatchState matchState);
}
