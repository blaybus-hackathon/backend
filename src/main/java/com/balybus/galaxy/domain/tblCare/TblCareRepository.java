package com.balybus.galaxy.domain.tblCare;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblCareRepository extends JpaRepository<TblCare, Long> {
    List<TblCare> findByCare_IdAndCareYnTrue(Long topCareSeq);
}
