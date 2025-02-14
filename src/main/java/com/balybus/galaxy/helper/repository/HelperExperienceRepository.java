package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface HelperExperienceRepository extends JpaRepository<TblHelperExperience, Long> {
    int countByHelperAndFieldAndHeStartDateAndHeEndDate(TblHelper helper, String field, @NotNull LocalDateTime heStartDate, LocalDateTime heEndDate);
}
