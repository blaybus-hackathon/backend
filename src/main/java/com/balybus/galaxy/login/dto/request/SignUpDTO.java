package com.balybus.galaxy.login.dto.request;

import com.balybus.galaxy.login.domain.type.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String essentialCertNo;
    private String socialCertNo;
    private String nurseCertNo;
    private boolean carOwnYn;
    private boolean eduYn;
    private int wage;

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
