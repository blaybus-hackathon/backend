package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.domain.TblHelperExperience;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HelperExperienceRepository extends JpaRepository<TblHelperExperience, Long> {
    int countByHelperAndFieldAndHeStartDateAndHeEndDate(TblHelper helper, String field, @NotNull LocalDateTime heStartDate, LocalDateTime heEndDate);
    List<TblHelperExperience> findByHelper(TblHelper helper);
}
