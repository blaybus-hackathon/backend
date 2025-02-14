package com.balybus.galaxy.address.repository;

import com.balybus.galaxy.address.domain.TblAddressFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblAddressFirstRepository extends JpaRepository<TblAddressFirst, Long> {
}
