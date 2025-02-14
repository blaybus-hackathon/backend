package com.balybus.galaxy.address.repository;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblAddressSecondRepository extends JpaRepository<TblAddressSecond, Long> {
}
