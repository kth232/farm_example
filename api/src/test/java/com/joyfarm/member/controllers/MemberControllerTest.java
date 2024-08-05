package com.joyfarm.member.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyfarm.global.rests.JSONData;
import com.joyfarm.member.services.MemberSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private MemberSaveService saveService;

    //@Autowired 하면 안됨
    private RequestJoin form;


    @BeforeEach
    void init() {
        form = new RequestJoin();
        form.setEmail("user01@test.org");
        form.setPassword("User1234!");
        form.setConfirmPassword(form.getPassword());
        form.setUserName("user01");
        form.setMobile("010-1111-2222");
        form.setAgree(true);

        saveService.save(form);
    }

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
                        .characterEncoding(Charset.forName("UTF-8"))
                        .content(params)) //바디
                .andDo(print());
    }

    @Test
    @DisplayName("token 발급 테스트")
    void tokenTest() throws Exception{

        RequestLogin loginForm = new RequestLogin();
        loginForm.setEmail(form.getEmail());
        loginForm.setPassword(form.getPassword() /*+ "****"*/);
        //없을 시 메세지 출력되는 지도 확인

        String params = om.writeValueAsString(loginForm);

        String body = mockMvc.perform(post("/account/token") //바디 데이터 반환
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(params)
                ).andDo(print())
                        .andReturn().getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        JSONData data = om.readValue(body, JSONData.class); //JSON 데이터로 바꾸기
        String token = (String)data.getData();

        mockMvc.perform(get("/account/test2")
                        .header("Authorization", "Bearer " + token))
                .andDo(print());



    }

}
