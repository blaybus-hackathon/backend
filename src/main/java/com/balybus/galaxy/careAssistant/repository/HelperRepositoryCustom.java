package com.balybus.galaxy.careAssistant.repository;

import com.balybus.galaxy.careAssistant.domain.TblHelper;
import com.balybus.galaxy.careAssistant.dto.request.HelperSearchRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelperRepositoryCustom {
    List<TblHelper> searchHelpers(HelperSearchRequest request);
}

