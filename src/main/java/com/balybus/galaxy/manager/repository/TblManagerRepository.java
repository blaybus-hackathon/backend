package com.balybus.galaxy.manager.repository;

import com.balybus.galaxy.manager.domain.TblManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblManagerRepository extends JpaRepository<TblManager, Long> {
    Optional<TblManager> findByUserSeq(Long userSeq);
}
