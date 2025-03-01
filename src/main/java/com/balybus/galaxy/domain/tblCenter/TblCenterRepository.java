package com.balybus.galaxy.domain.tblCenter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblCenterRepository extends JpaRepository<TblCenter, Long> {
    Optional<TblCenter> findByCenterAddress(String address);
    Optional<TblCenter> findByCenterCode(String centerCode);
}
