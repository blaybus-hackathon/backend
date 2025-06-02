package com.balybus.galaxy.global.domain.tblCenterManager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblCenterManagerRepository extends JpaRepository<TblCenterManager, Long> {

    Optional<TblCenterManager> findByMember_Id(Long userSeq);
}
