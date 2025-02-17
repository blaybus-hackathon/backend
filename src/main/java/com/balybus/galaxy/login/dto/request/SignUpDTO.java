package com.balybus.galaxy.login.dto.request;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignUpDTO {
    private String email;
    private String password;
    private RoleType roleType;
    private String name;
    private String phone;
    private Integer gender;
    private String birthday;
    private String addressDetail;
    private String essentialCertNo; // 요양 보호사
    private String careCertNo;
    private String nurseCertNo;
    private String postPartumCertNo;
    private List<String> helperOtherCerts;
    private boolean carOwnYn;
    private boolean eduYn;

    public static boolean hasNullDataBeforeSignUp(SignUpDTO dto) {
        return dto.getEmail() == null ||
                dto.getPassword() == null ||
                dto.getRoleType() == null ||
                dto.getName() == null ||
                dto.getPhone() == null ||
                dto.getAddressDetail() == null ||
                dto.getEssentialCertNo() == null;
    }
}
