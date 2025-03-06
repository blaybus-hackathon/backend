package com.balybus.galaxy.helper.repository;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.dto.request.HelperSearchRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperRepositoryCustom {
    List<TblHelper> searchHelpers(HelperSearchRequest request);
}

