package com.balybus.galaxy.Center.repository;

import com.balybus.galaxy.Center.domain.TblSignUpCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<TblSignUpCenter, Long> {
}