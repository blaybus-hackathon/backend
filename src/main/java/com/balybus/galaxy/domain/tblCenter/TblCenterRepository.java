package com.balybus.galaxy.domain.tblCenter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TblCenterRepository extends JpaRepository<TblCenter, Long> {
    Optional<TblCenter> findByCenterAddress(String address);
    Optional<TblCenter> findByCenterCode(String centerCode);


    // 센터명/주소/발급코드
    Page<TblCenter> findByCenterNameContainsOrCenterAddressContainsOrCenterCode(String searchName, String searchAddress, String searchCode, Pageable page);
}
