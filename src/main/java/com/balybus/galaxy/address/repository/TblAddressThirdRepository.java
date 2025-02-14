package com.balybus.galaxy.address.repository;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import com.balybus.galaxy.address.domain.TblAddressThird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblAddressThirdRepository extends JpaRepository<TblAddressThird, Long> {
}
