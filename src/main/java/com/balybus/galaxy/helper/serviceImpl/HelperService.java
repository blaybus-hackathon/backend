package com.balybus.galaxy.helper.serviceImpl;

import com.balybus.galaxy.helper.dto.request.HelperExperienceDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkLocationDTO;
import com.balybus.galaxy.helper.dto.request.HelperWorkTimeRequestDTO;
import com.balybus.galaxy.helper.dto.response.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface HelperService {
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails);
    public List<TblAddressFirstDTO> getFirstAddress();
    public List<TblAddressThirdDTO> getThirdAddressBySecondId(Long asSeq);
    public List<TblAddressSecondDTO> getAddressSecondByFirstId(Long afSeq);
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails);
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails);
}
