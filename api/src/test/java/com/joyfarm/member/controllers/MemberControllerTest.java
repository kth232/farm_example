package com.joyfarm.member.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() throws Exception {
        RequestJoin form = new RequestJoin();
//        form.setEmail("user01@test.org");
//        form.setPassword("User1234!");
//        form.setConfirmPassword(form.getPassword());
//        form.setUserName("user01");
//        form.setMobile("010-1111-2222");
//        form.setAgree(true);

        String params = om.writeValueAsString(form); //JSON 문자열로 변환

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(params)) //바디
                .andDo(print());
    }
}
