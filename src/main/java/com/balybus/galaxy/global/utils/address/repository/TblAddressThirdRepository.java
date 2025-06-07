package com.balybus.galaxy.global.utils.address.repository;

import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TblAddressThirdRepository extends JpaRepository<TblAddressThird, Long> {
    List<TblAddressThird> findByAddressSecond_Id(Long asSeq);
    Optional<TblAddressThird> findByAddressSecond_IdAndId(Long asSeq, Long atSeq);
}
