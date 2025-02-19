package com.balybus.galaxy.global.utils.matching;

import com.balybus.galaxy.domain.tblMatching.TblMatching;
import com.balybus.galaxy.domain.tblMatching.TblMatchingRepository;
import com.balybus.galaxy.global.utils.matching.dto.MatchingResponseDto;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.patient.domain.TblPatientLog;
import com.balybus.galaxy.patient.repository.TblPatientLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingUtils {
    private final TblMatchingRepository matchingRepository;
    private final TblPatientLogRepository patientLogRepository;
    private final HelperRepository helperRepository;

    @Async
    @Transactional
    public void matchingSystem(Long plSeq){
        //1. 노인정보를 조회한다.
        Optional<TblPatientLog> plOpt = patientLogRepository.findById(plSeq);
        if(plOpt.isEmpty()) return;
        TblPatientLog plEntity = plOpt.get();

        //2. 기존 매칭 데이터 조회 및 미사용 처리
        List<TblMatching> bfMatchingList = matchingRepository.findByPatientLog_id(plSeq);
        for(TblMatching entity : bfMatchingList) entity.useNo();

        //3. 근무지 / 근무가능 시간 / 전문성 / 경력 / 자격증 개수 계산을 한 조회 를 해서 최대 10개의 요양보호사 데이터를 가져온다
        List<MatchingResponseDto.Score> helperScoreList = helperRepository.findTop10HelperScores(plSeq);

        //4. 매칭 테이블에 데이터 저장 - 데이터 저장시 기존에 공고-요양보호사가 같은 조건 데이터 존재 확인
        List<TblMatching> saveMatchingList = new ArrayList<>();
        for(MatchingResponseDto.Score dto : helperScoreList) {
            Optional<TblMatching> bfOpt = matchingRepository.findByPatientLog_idAndHelper_id(plSeq, dto.getHelperSeq());
            saveMatchingList.add(dto.toEntity(bfOpt.orElse(null), plEntity));
        }
        matchingRepository.saveAll(saveMatchingList);

        //5. 매칭 결과 이메일 전송
        // ooo님의 00명의 요양보호사가 매칭되었습니다.

    }
}
