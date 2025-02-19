package com.balybus.galaxy.domain.tblMatching;

import com.balybus.galaxy.global.utils.mail.ContentType;
import com.balybus.galaxy.global.utils.mail.SendMailRequest;
import com.balybus.galaxy.global.utils.mail.SendMailUtils;
import com.balybus.galaxy.global.utils.mail.dto.contents.ContentDto;
import com.balybus.galaxy.global.utils.mail.dto.contents.MailMatchingDto;
import com.balybus.galaxy.domain.tblMatching.dto.MatchingResponseDto;
import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.repositoryImpl.HelperRepository;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLog;
import com.balybus.galaxy.patient.domain.tblPatientLog.TblPatientLogRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingServiceImpl {
    private final TblMatchingRepository matchingRepository;
    private final TblPatientLogRepository patientLogRepository;
    private final HelperRepository helperRepository;
    private final SendMailUtils sendMailUtils;

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
        List<MailMatchingDto.HelperContentDto> mailContentList = new ArrayList<>(); // 메일 전송 요양보호사 정보 리스트
        List<TblMatching> saveMatchingList = new ArrayList<>(); // 매칭 테이블 저장 데이터
        for(MatchingResponseDto.Score dto : helperScoreList) {
            Optional<TblMatching> bfOpt = matchingRepository.findByPatientLog_idAndHelper_id(plSeq, dto.getHelperSeq());

            // 4-1. 요양 보호사 조회
            Optional<TblHelper> helperOpt = helperRepository.findById(dto.getHelperSeq());
            if(helperOpt.isEmpty()) continue;
            TblHelper helperEntity = helperOpt.get();

            TblMatching matching = dto.toEntity(bfOpt.orElse(null), helperEntity, plEntity);
            saveMatchingList.add(matching);
            mailContentList.add(MailMatchingDto.HelperContentDto.builder()
                            .name(helperEntity.getName())
                            .gender(helperEntity.getGender() == 1 ? "남" : "여")
                            .age(calculateAge(LocalDate.parse(helperEntity.getBirthday(), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
                            .build());
        }
        matchingRepository.saveAll(saveMatchingList);

        //5. 매칭 결과 이메일 전송
        // ooo님의 조건에 유사한 요양보호사 00명의 추천리스트가 완성되었습니다.
        //5-1.이메일 내용 객체 생성
        MailMatchingDto mailMatchingDto = MailMatchingDto.builder()
                .plName(plEntity.getName())
                .contentList(mailContentList)
                .build();
        ContentDto<MailMatchingDto> contentDto = new ContentDto<>(mailMatchingDto);

        //3. 인증코드 이메일 전송
        SendMailRequest request = SendMailRequest.builder()
                .toMail(plEntity.getManager().getMember().getEmail()) // 관리자 이메일
                .title(plEntity.getName()+"님의 매칭 추천 리스트") // 어르신 성함 + "매칭 추천 리스트"
                .fromName("은하수 개발단")
                .contentType(ContentType.MATCHING_LIST)
                .build();
        try {
            sendMailUtils.sendMail(request, contentDto);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        // 생일과 현재 날짜의 차이 계산
        Period period = Period.between(birthDate, currentDate);
        // 나이는 Period 객체의 연도를 반환
        return period.getYears();
    }
}
