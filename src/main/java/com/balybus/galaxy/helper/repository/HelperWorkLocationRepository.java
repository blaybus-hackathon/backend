package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelperWorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelperWorkLocationRepository extends JpaRepository<TblHelperWorkLocation, Long> {
}
