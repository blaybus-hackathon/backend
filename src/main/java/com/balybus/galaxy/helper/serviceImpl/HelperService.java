package com.balybus.galaxy.helper.serviceImpl;

import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface HelperService {
    public HelperResponse getAllHelperInfo(UserDetails userDetails);
    public void updateProfile(UserDetails userDetails, HelperProfileDTO helperProfileDTO);
    public WageUpdateResponse updateWage(UserDetails userDetails, WageUpdateDTO wageUpdateDTO);
    public HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails);
    public List<TblAddressFirstResponse> getFirstAddress();
    public List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq);
    public List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq);
    public HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails);
    public HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails);
}
