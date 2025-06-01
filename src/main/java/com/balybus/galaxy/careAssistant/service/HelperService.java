package com.balybus.galaxy.careAssistant.service;

import com.balybus.galaxy.careAssistant.dto.request.*;
import com.balybus.galaxy.careAssistant.dto.response.*;
import com.balybus.galaxy.login.classic.dto.request.HelperCertDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface HelperService {
    HelperResponse getAllHelperInfo(UserDetails userDetails);
    void updateProfile(UserDetails userDetails, HelperProfileDTO helperProfileDTO);
    WageUpdateResponse updateWage(UserDetails userDetails, WageUpdateDTO wageUpdateDTO);
    HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails);
    HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails);
    HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails);
    HelperSearchResponse helperSearch(HelperSearchDTO helperSearchDTO);
    Map<String, String> saveCertificateByQNet(List<HelperCertDTO> helperCertDTO, UserDetails userDetails);
    String checkCertificate(String name, String birth, String certNo, String issueDate, String insideNo);

}
