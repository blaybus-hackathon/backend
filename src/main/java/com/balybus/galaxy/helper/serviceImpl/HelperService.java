package com.balybus.galaxy.helper.serviceImpl;

import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.*;

import java.util.List;

public interface HelperService {
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO);
    public List<TblAddressFirstDTO> getFirstAddress();
    public List<TblAddressThirdDTO> getThirdAddressBySecondId(Long asSeq);
    public List<TblAddressSecondDTO> getAddressSecondByFirstId(Long afSeq);
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO);
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO);
}
