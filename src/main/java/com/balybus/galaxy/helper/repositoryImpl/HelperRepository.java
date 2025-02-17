package com.balybus.galaxy.helper.repositoryImpl;

import com.balybus.galaxy.helper.domain.TblHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HelperRepository extends JpaRepository<TblHelper, Long> {
    Optional<TblHelper> findByUserId(Long userId);
}
