package com.balybus.galaxy.global.utils.address.repository;

import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblAddressSecondRepository extends JpaRepository<TblAddressSecond, Long> {
    List<TblAddressSecond> findByAddressFirst_Id(Long afSeq);
    Optional<TblAddressSecond> findByAddressFirstIdAndId(Long firstSeq, Long secondSeq);
}
