package com.joyfarm.email;

import com.joyfarm.email.controllers.EmailMessage;
import com.joyfarm.email.services.EmailSendService;
import com.joyfarm.email.services.EmailVerifyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("Test")
public class EmailSendTest {
    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailVerifyService emailVerifyService;

    @Test
    @DisplayName("이메일 전송 테스트")
    void sendTest() {
        EmailMessage message = new EmailMessage("kth7537@naver.com", "제목...", "내용...");
        boolean success = emailSendService.sendMail(message);

        assertTrue(success);
    }


}