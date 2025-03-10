package com.balybus.galaxy.center.repository;

import com.balybus.galaxy.center.entity.CenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CenterListRepository extends JpaRepository<CenterEntity, Long> {

    @Query("SELECT c FROM CenterEntity c " +
            "WHERE (:searchType = '전체' OR " +
            "(:searchType = '센터명' AND c.name LIKE %:keyword%) OR " +
            "(:searchType = '주소' AND c.address LIKE %:keyword%) OR " +
            "(:searchType = '발급코드' AND c.issueCode LIKE %:keyword%))")
    List<CenterEntity> searchCenters(@Param("searchType") String searchType, @Param("keyword") String keyword);
}
