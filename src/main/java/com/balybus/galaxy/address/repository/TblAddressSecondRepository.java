package com.balybus.galaxy.address.repository;

import com.balybus.galaxy.address.domain.TblAddressSecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblAddressSecondRepository extends JpaRepository<TblAddressSecond, Long> {
    List<TblAddressSecond> findByAddressFirst_Id(Long afSeq);
}
