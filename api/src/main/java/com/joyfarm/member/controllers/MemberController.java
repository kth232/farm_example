package com.joyfarm.member.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Controller
public class MemberController {

    @GetMapping("/token")
    public String token() {
        return "token";
    }
}
