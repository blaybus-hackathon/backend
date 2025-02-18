package com.balybus.galaxy.global.utils.matching;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingUtils {

    public void matchingSystem(){
        //1. 노인정보를 조회한다.
        //2. 근무지 - 거리 노인의 주소 정보와 요양보호사의 근무희망지가 일치하는 리스트를 출력한다.
        //3. 근무가능 시간 - (겹치는 시간이 많을수록 높은 점수)
        //4. 전문성 (노인의 케어 필요 항목과 요양 보호사의 전문 분야 일치 여부)
        //5. 경력 (경력이 길수록 높은 점수)
        //6. 자격증 개수 및 등급 (사회복지사/간호조무사 추가 보유 시 가산점)

    }
}
