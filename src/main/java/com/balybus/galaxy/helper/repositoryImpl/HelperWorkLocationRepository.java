package com.balybus.galaxy.helper.repositoryImpl;

import com.balybus.galaxy.helper.domain.TblHelperWorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelperWorkLocationRepository extends JpaRepository<TblHelperWorkLocation, Long> {
}
