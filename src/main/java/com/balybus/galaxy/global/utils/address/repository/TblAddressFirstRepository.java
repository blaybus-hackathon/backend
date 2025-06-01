package com.balybus.galaxy.global.utils.address.repository;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblAddressFirstRepository extends JpaRepository<TblAddressFirst, Long> {
}
