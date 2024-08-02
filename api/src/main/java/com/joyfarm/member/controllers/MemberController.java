package com.joyfarm.member.controllers;


import com.joyfarm.global.Utils;
import com.joyfarm.global.exceptions.BadRequestException;
import com.joyfarm.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class MemberController {

    private final JoinValidator joinValidator;
    private final Utils utils;

    @PostMapping//회원 가입
    public ResponseEntity join(@RequestBody @Valid RequestJoin form, Errors errors) { //가입 시 응답 코드만 내보내기

        joinValidator.validate(form, errors);

        if(errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/token") //로그인
    public String token() {

        return "token";
    }
}
