package com.balybus.galaxy.domain.tblCare;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblCareRepository extends JpaRepository<TblCare, Long> {
    List<TblCare> findByCare_IdAndCareYnTrue(Long topCareSeq);

    @Query(value = """
            select care_seq
            from tbl_care tc
            where care_yn = true
            and top_care_seq = :topCareSeq
            and care_val & :calValTotal <> 0
            """, nativeQuery = true)
    List<Long> findByCareIdList(@Param("topCareSeq") Long topCareSeq, @Param("calValTotal") int calValTotal);

    @Query(value = """
            select group_concat(care_name separator ', ')
            from tbl_care tc
            where care_yn = true
            and top_care_seq = :topCareSeq
            and care_val & :calValTotal <> 0
            """, nativeQuery = true)
    String findCalNameListStr(@Param("topCareSeq") Long topCareSeq, @Param("calValTotal") int calValTotal);
}
