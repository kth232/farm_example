package com.joyfarm.global.advices;

import com.joyfarm.file.entities.FileInfo;
import com.joyfarm.member.MemberUtil;
import com.joyfarm.member.entities.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice("com.joyfarm") //범위 설정
public class CommonControllerAdvice { //전역에서 확인 가능

    private final MemberUtil memberUtil;
    @ModelAttribute("loggedMember")
    public Member loggedMember(){
        return memberUtil.getMember();
    }
    @ModelAttribute("isLogin")
    public boolean isLogin(){
        return memberUtil.isLogin();
    }
    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return memberUtil.isAdmin();
    }

    @ModelAttribute("myProfileImage")
    public FileInfo myProfileImage() {
        if (isLogin()) {
            Member member = memberUtil.getMember();
            return member.getProfileImage();
        }

        return null;
    }

}