package com.balybus.galaxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MailConfigTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void javaMailSender_빈이_정상적으로_등록되는지_테스트() {
        assertThat(mailSender).isNotNull();
    }
}
