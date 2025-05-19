package com.balybus.galaxy.helper.serviceImpl;

import com.balybus.galaxy.helper.aspect.AccessorHelper;
import com.balybus.galaxy.helper.dto.request.*;
import com.balybus.galaxy.helper.dto.response.*;
import com.balybus.galaxy.login.dto.request.HelperCertDTO;
import com.balybus.galaxy.login.dto.request.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface HelperService {
    HelperResponse getAllHelperInfo(UserDetails userDetails);
    void updateProfile(AccessorHelper accessorHelper, HelperProfileDTO helperProfileDTO);
    WageUpdateResponse updateWage(UserDetails userDetails, WageUpdateDTO wageUpdateDTO);
    HelperWorkLocationReponse workLocationSignUp(HelperWorkLocationDTO helperWorkLocationDTO, UserDetails userDetails);
    List<TblAddressFirstResponse> getFirstAddress();
    List<TblAddressThirdResponse> getThirdAddressBySecondId(Long asSeq);
    List<TblAddressSecondResponse> getAddressSecondByFirstId(Long afSeq);
    HelperWorkTimeResponse workTimeSignUp(HelperWorkTimeRequestDTO helperWorkTimeRequestDTO, UserDetails userDetails);
    HelperExperienceResponse experienceSignUp(HelperExperienceDTO helperExperienceDTO, UserDetails userDetails);
    HelperSearchResponse helperSearch(HelperSearchDTO helperSearchDTO);
    Map<String, String> saveCertificateByQNet(List<HelperCertDTO> helperCertDTO, UserDetails userDetails);
    String checkCertificate(String name, String birth, String certNo, String issueDate, String insideNo);

}
