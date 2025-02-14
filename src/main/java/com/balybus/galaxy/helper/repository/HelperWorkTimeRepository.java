package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperWorkTime;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HelperWorkTimeRepository extends JpaRepository<TblHelperWorkTime, Long> {
    int countByHelperAndDateAndStartTimeAndEndTime(
            TblHelper helper, @NotNull Integer date, @NotNull Float startTime, @NotNull Float endTime
    );
}
