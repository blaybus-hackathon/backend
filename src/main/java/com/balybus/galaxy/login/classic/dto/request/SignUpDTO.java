package com.balybus.galaxy.login.classic.dto.request;

import com.balybus.galaxy.login.classic.domain.type.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class    SignUpDTO {
    private RoleType roleType;
    private String email;
    private String password;
    private String name; // 이름
    private String phone; // 전화번호
    private Integer gender; // 성별
    private String birthday; // 생년 월일
    private Integer profilePic; // 프로필 사진 등록 여부
    private String addressDetail; //  주소
    private HelperCertDTO essentialCertNo; // 요양 보호사 자격증 번호
    private HelperCertDTO careCertNo; // 간병사 자격증 번호
    private HelperCertDTO nurseCertNo; // 병원 동행 매니저 자격증 번호
    private HelperCertDTO postPartumCertNo; // 산후 관리사 자격증 번호
    private HelperCertDTO helperOtherCerts; // 기타 자격증
    private boolean carOwnYn; // 차량 소유 여부
    private boolean eduYn; // 치매 교육 이수 여부

//    public static boolean   hasNullDataBeforeSignUp(SignUpDTO dto) {
//        return dto.getRoleType() == null ||
//                dto.getName() == null ||
//                dto.getPhone() == null ||
//                dto.getAddressDetail() == null;
//    }

    public boolean hasNullDataBeforeSignUp(){
        return this.roleType == null ||
                this.name == null ||
                this.phone == null ||
                this.addressDetail == null;
    }
}
