package com.balybus.galaxy.global.temp;

import com.balybus.galaxy.global.utils.mail.ContentType;
import com.balybus.galaxy.global.utils.mail.SendMailRequest;
import com.balybus.galaxy.global.utils.mail.SendMailUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TempService {

    private final SendMailUtils sendMailUtils;

    // 이메일 인증
    public void authenticationMail(String userEmail) {
        String tempCode = createTempCode();
        sendingMail(userEmail, tempCode);
    }

    /**
     * 메일 전송
     * @param userEmail String
     * @param tempPassword String
     */
    private void sendingMail(String userEmail, String tempPassword){
        SendMailRequest request = SendMailRequest.builder()
                .toMail(userEmail)
                .title("Test :: 제목")
                .fromName("Test :: 은하수 개발단")
                .contentType(ContentType.AUTHENTICATION)
                .content(tempPassword)
                .build();
        try {
            sendMailUtils.sendMail(request);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 임시 비밀번호 생성
     * @return String
     */
    private String createTempCode(){
        int length = 15;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^*()";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
