package com.balybus.galaxy.notice.reporitory;

import com.balybus.galaxy.notice.domain.TblNoticePatient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<TblNoticePatient, Long> {
}
