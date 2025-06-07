package com.balybus.galaxy.careAssistant.repository;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.careAssistant.domain.TblHelperWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperWorkTimeRepository extends JpaRepository<TblHelperWorkTime, Long> {

    // 특정 요양보호사의 근무 시간 목록 조회
    List<TblHelperWorkTime> findByHelper(TblHelper helper);

    // 같은 날짜, 시간, 기간을 가진 WorkTime이 존재하는지 확인
    boolean existsByHelperAndDateAndStartTimeAndEndTime(
            TblHelper helper, Integer date, String startTime, String endTime);
}


