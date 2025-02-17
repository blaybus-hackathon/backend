package com.balybus.galaxy.helper.repositoryImpl;

import com.balybus.galaxy.helper.domain.TblHelper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelperRepository extends JpaRepository<TblHelper, Long>, HelperRepositoryCustom {
    // 기본적인 CRUD 메서드는 JpaRepository가 제공
}
