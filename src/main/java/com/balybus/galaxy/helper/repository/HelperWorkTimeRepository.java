package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperWorkTimeRepository extends JpaRepository<TblHelperWorkTime, Long> {

    // 특정 요양보호사의 근무 시간 목록 조회
    List<TblHelperWorkTime> findByHelper(TblHelper helper);
}

