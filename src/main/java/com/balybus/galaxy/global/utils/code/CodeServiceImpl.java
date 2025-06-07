package com.balybus.galaxy.global.utils.code;

import com.balybus.galaxy.global.domain.tblAddressFirst.TblAddressFirst;
import com.balybus.galaxy.global.domain.tblAddressSecond.TblAddressSecond;
import com.balybus.galaxy.global.domain.tblAddressThird.TblAddressThird;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
public class CodeServiceImpl implements CodeService {
    /**
     * 나이 계산기(만 나이 반환)
     * @param birthDate LocalDate:생년월일
     * @return int:만 나이
     */
    @Override
    public int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        // 생일과 현재 날짜의 차이 계산
        Period period = Period.between(birthDate, currentDate);
        // 나이는 Period 객체의 연도를 반환
        return period.getYears();
    }

    /**
     * 주소 이름 정리
     * @param first TblAddressFirst:광역시.도
     * @param second TblAddressSecond:시.군.구
     * @param third TblAddressThird:읍.면.동
     * @return String
     */
    @Override
    public String fullAddressString(TblAddressFirst first, TblAddressSecond second, TblAddressThird third) {
        String address = first.getName();

        if(first.getId() * 1000 == second.getId()) address = second.getName();
        else if (second.getId() * 1000 == third.getId()) address += " " + third.getName();
        else address += " " + second.getName() + " " + third.getName();

        return address;
    }
}
