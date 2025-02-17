package com.balybus.galaxy.helper.repositoryImpl;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.dto.request.HelperSearchRequest;

import java.util.List;

public interface HelperRepositoryCustom {
    List<TblHelper> searchHelpers(HelperSearchRequest request);
}

