package com.balybus.galaxy.careAssistant.repository;

import com.balybus.galaxy.careAssistant.domain.TblHelperCert;
import com.balybus.galaxy.careAssistant.domain.TblHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperCertRepository extends JpaRepository<TblHelperCert, Long> {
    List<TblHelperCert> findAllById(Long helperId);
    List<TblHelperCert> findByTblHelperId(Long helperId);
    List<TblHelperCert> findByTblHelper(TblHelper tblHelper);
}
