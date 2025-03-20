package com.balybus.galaxy.domain.tblMatching;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblMatchingRepository extends JpaRepository<TblMatching, Long> {
    List<TblMatching> findByPatientLog_id(Long plSeq);
    Optional<TblMatching> findByPatientLog_idAndHelper_id(Long plSeq, Long helperSeq);
    @Query("SELECT t FROM TblMatching t WHERE t.helper.id = :helperId AND t.matchState = :matchState")
    List<TblMatching> findMatchingRequests(@Param("helperId") Long helperId, @Param("matchState") MatchState matchState);
}
