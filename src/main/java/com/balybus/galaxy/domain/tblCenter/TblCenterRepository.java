package com.balybus.galaxy.domain.tblCenter;

import com.balybus.galaxy.domain.tblCenterManager.TblCenterManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblCenterRepository extends JpaRepository<TblCenter, Long> {
}
