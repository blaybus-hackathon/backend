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
        //2. 근무지 / 근무가능 시간 / 전문성 / 경력 / 자격증 개수 계산을 한 조회 결과 가져오기


    }
}
