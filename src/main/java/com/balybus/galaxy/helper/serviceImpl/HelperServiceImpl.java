package com.balybus.galaxy.helper.serviceImpl;

import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.HelperExperienceResponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkLocationReponse;
import com.balybus.galaxy.helper.dto.response.HelperWorkTimeResponse;

public interface HelperServiceImpl {
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO);
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO);
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO);
}
