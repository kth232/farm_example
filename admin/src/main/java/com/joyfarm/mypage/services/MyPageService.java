package com.joyfarm.mypage.services;

import com.joyfarm.member.services.MemberSaveService;
import com.joyfarm.mypage.controllers.RequestProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberSaveService saveService;

    public void update(RequestProfile form) {

    }
}