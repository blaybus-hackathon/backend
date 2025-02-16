package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.member.domain.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelperRepository extends JpaRepository<TblHelper, Long> {
    Optional<TblHelper> findByUserId(Long userId);
}
