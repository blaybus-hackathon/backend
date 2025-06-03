package com.balybus.galaxy.careAssistant.repository;

import com.balybus.galaxy.careAssistant.domain.TblHelperWorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelperWorkLocationRepository extends JpaRepository<TblHelperWorkLocation, Long> {
}
