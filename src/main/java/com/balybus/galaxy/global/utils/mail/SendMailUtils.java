package com.balybus.galaxy.global.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SendMailUtils {
    private final JavaMailSender javaMailSender;
    private final JavaMailSenderImpl javaMailSenderImpl;
    private final SpringTemplateEngine templateEngine;

    /**
     * 메일을 전송합니다.
     * @param request 메일 데이터
     */
    public void sendMail(SendMailRequest request) throws UnsupportedEncodingException, MessagingException {
        // 클래스 생성
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        // 메일 발신자 설정
        mimeMessageHelper.setFrom(javaMailSenderImpl.getUsername(), request.getFromName());

        // 메일 수신자 설정
        mimeMessageHelper.setTo(request.getToMail());

        // 메일 제목
        mimeMessageHelper.setSubject(request.getTitle());

        //메일 내용
        String html = createHtml(request.getContentType(), request.getContent());
        mimeMessageHelper.setText(html, true); // 메일 본문 내용, HTML 여부

        // 메일 전송
        javaMailSender.send(mimeMessage);
        log.info("Succeeded to send Email");
    }

    /**
     * 타임리프를 사용하여 메일 내용을 생성합니다.
     */
    private String createHtml(ContentType contentType, String content) {
        Context context = new Context();
        context.setVariable("content", content);
        return templateEngine.process(contentType.getUri(), context);
    }
}
