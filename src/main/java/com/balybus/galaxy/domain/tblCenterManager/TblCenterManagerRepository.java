package com.balybus.galaxy.domain.tblCenterManager;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.member.domain.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblCenterManagerRepository extends JpaRepository<TblCenterManager, Long> {
    Optional<TblCenterManager> findByMember(TblUser tblUser); // TblCenterManager를 찾기 위한 메서드
}
