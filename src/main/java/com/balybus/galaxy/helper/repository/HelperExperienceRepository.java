package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import com.balybus.galaxy.helper.domain.TblHelperWorkTime;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HelperExperienceRepository extends JpaRepository<TblHelperExperience, Long> {
    int countByHelperAndFieldAndHeStartDateAndHeEndDate(TblHelper helper, String field, @NotNull LocalDateTime heStartDate, LocalDateTime heEndDate);
    List<TblHelperExperience> findByHelper(TblHelper helper);
}
