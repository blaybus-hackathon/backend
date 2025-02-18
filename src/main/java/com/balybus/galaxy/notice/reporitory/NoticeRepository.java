package com.balybus.galaxy.notice.reporitory;

import com.balybus.galaxy.patient.domain.TblPatient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<TblPatient, Long> {
}

