package com.joyfarm.member.jwt;

import com.joyfarm.member.controllers.RequestJoin;
import com.joyfarm.member.services.MemberSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TokenProviderTest {
    @Autowired
    private TokenProvider provider;

    @Autowired
    private MemberSaveService saveService;

    private RequestJoin form;

    @BeforeEach
    void init() {
        form = new RequestJoin();
        form.setEmail("user01@test.org");
        form.setPassword("User1234!");
        form.setConfirmPassword(form.getPassword());
        form.setMobile("010-1000-1000");
        form.setUserName("user01");
        form.setAgree(true);
        saveService.save(form);
    }

    @Test
    @DisplayName("토큰 발급 테스트")
    @WithMockUser(username="user01@test.org", password="User1234!", authorities = "USER")
    void createTokenTest() {
        String token = provider.createToken("user01@test.org", "User1234!");
        System.out.println(token);
    }
}