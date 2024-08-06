package com.joyfarm.member.controllers;


import com.joyfarm.global.Utils;
import com.joyfarm.global.exceptions.BadRequestException;
import com.joyfarm.global.rests.JSONData;
import com.joyfarm.member.jwt.TokenProvider;
import com.joyfarm.member.services.MemberSaveService;
import com.joyfarm.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService saveService;
    private final TokenProvider tokenProvider;
    private final Utils utils;

    /* 회원 가입 시 응답 코드 201 */
    @PostMapping // /account 쪽에 Post 방식으로 접근하면 -> 회원가입
    public ResponseEntity join(@RequestBody @Valid RequestJoin form, Errors errors){
        // 회원 가입 정보는 JSON 데이터로 전달 -> @RequestBody
        //가입 시 응답 코드만 내보내기

        joinValidator.validate(form, errors);

        if (errors.hasErrors()){
            //errors.getAllErrors().stream().forEach(System.out::println);
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
        saveService.save(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //로그인 토큰으로 인증, post 방식으로 넘겨야 함<-RequestBody
    /* 로그인 절차 완료 시 토큰(=교환권) 발급 */
    @PostMapping("/token")
    public JSONData token(@RequestBody @Valid RequestLogin form, Errors errors){

        if (errors.hasErrors()){
            //검증 실패 시
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
        String token = tokenProvider.createToken(form.getEmail(), form.getPassword());

        return new JSONData(token); // 이상이 없으면 JSONData로 토큰 발급
    }

    @GetMapping("/test1")
    public void memberOnly() {
        log.info("user 전용");
    }

    @GetMapping("/test2")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void adminOnly() {
        log.info("admin 전용");
    }
}
