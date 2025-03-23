package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelperCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperCertRepository extends JpaRepository<TblHelperCert, Long> {
    List<TblHelperCert> findAllById(Long helperId);
}
