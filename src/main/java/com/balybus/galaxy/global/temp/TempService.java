//package com.balybus.galaxy.global.temp;
//
//import com.balybus.galaxy.global.domain.tblMatching.MatchingServiceImpl;
//import com.balybus.galaxy.global.utils.mail.ContentType;
//import com.balybus.galaxy.global.utils.mail.SendMailRequest;
//import com.balybus.galaxy.global.utils.mail.SendMailUtils;
//import com.balybus.galaxy.global.utils.mail.dto.contents.ContentDto;
//import com.balybus.galaxy.global.utils.mail.dto.contents.MailMatchingDto;
//import jakarta.mail.MessagingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.UnsupportedEncodingException;
//import java.time.LocalDate;
//import java.time.Period;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Slf4j
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class TempService {
//
//    private final SendMailUtils sendMailUtils;
//    private final MatchingServiceImpl matchingService;
//
//    // 이메일 인증
////    public void authenticationMail(String userEmail) {
////        String tempCode = createTempCode();
////        sendingMail(userEmail, tempCode);
////    }
//
//    /**
//     * 메일 전송
//     * @param userEmail String
//     * @param tempPassword String
//     */
////    private void sendingMail(String userEmail, String tempPassword){
////        SendMailRequest request = SendMailRequest.builder()
////                .toMail(userEmail)
////                .title("Test :: 제목")
////                .fromName("Test :: 은하수 개발단")
////                .contentType(ContentType.AUTHENTICATION)
////                .build();
////        ContentDto<String> contentDto = new ContentDto<>(tempPassword);
////
////        try {
////            sendMailUtils.sendMail(request, contentDto);
////        } catch (UnsupportedEncodingException | MessagingException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
//    /**
//     * 임시 비밀번호 생성
//     * @return String
//     */
////    private String createTempCode(){
////        int length = 15;
////        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^*()";
////        Random random = new Random();
////        StringBuilder sb = new StringBuilder(length);
////        for (int i = 0; i < length; i++) {
////            int index = random.nextInt(characters.length());
////            sb.append(characters.charAt(index));
////        }
////        return sb.toString();
////    }
//
////    public void matchingMail() {
////        List<MailMatchingDto.HelperContentDto> mailContentList = new ArrayList<>(); // 메일 전송 요양보호사 정보 리스트
////        for(int i = 1; i <= 9; i ++) {
////            mailContentList.add(MailMatchingDto.HelperContentDto.builder()
////                    .name("요양보호사"+i)
////                    .gender(i%2 == 1 ? "남" : "여")
////                    .age(calculateAge(LocalDate.parse(Integer.toString(19970215+i), java.time.format.DateTimeFormatter.BASIC_ISO_DATE)))
////                    .build());
////        }
////
////        MailMatchingDto mailMatchingDto = MailMatchingDto.builder()
////                .plName("어르신 이름")
////                .contentList(mailContentList)
////                .build();
////        ContentDto<MailMatchingDto> contentDto = new ContentDto<>(mailMatchingDto);
////
////        //3. 인증코드 이메일 전송
////        SendMailRequest request = SendMailRequest.builder()
////                .toMail("ella.code.j@gmail.com") // 관리자 이메일
////                .title("어르신 이름"+"님의 매칭 추천 리스트") // 어르신 성함 + "매칭 추천 리스트"
////                .fromName("은하수 개발단")
////                .contentType(ContentType.MATCHING_LIST)
////                .build();
////        try {
////            sendMailUtils.sendMail(request, contentDto);
////        } catch (UnsupportedEncodingException | MessagingException e) {
////            throw new RuntimeException(e);
////        }
////    }
//
////    private int calculateAge(LocalDate birthDate) {
////        LocalDate currentDate = LocalDate.now();
////        // 생일과 현재 날짜의 차이 계산
////        Period period = Period.between(birthDate, currentDate);
////        // 나이는 Period 객체의 연도를 반환
////        return period.getYears();
////    }
//
//
////    public void matching(){
////        matchingService.matchingSystem(1L);
////    }
//}
