package com.joyfarm.member.services;

import com.joyfarm.file.services.FileUploadDoneService;
import com.joyfarm.member.MemberUtil;
import com.joyfarm.member.constants.Authority;
import com.joyfarm.member.controllers.RequestJoin;
import com.joyfarm.member.entities.Authorities;
import com.joyfarm.member.entities.Member;
import com.joyfarm.member.exceptions.MemberNotFoundException;
import com.joyfarm.member.repositories.AuthoritiesRepository;
import com.joyfarm.member.repositories.MemberRepository;
import com.joyfarm.mypage.controllers.RequestProfile;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSaveService {
    private final FileUploadDoneService uploadDoneService;
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
    private final HttpSession session;

    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {
        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(form.getPassword()); // BCrypt 해시화
        member.setPassword(hash);

        save(member, List.of(Authority.USER));
    }

    /**
     * 회원정보 수정
     * @param form
     */
    public void save(RequestProfile form) {
        Member member = memberUtil.getMember();
        String email = member.getEmail();
        member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        String password = form.getPassword();
        String mobile = form.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }

        member.setUserName(form.getUserName());
        member.setMobile(mobile);

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }

        memberRepository.saveAndFlush(member);

        session.setAttribute("userInfoChanged", true);
    }

    public void save(Member member, List<Authority> authorities) {
        //휴대폰 번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(authority -> Authorities.builder()
                    .member(member)
                    .authority(authority)
                    .build()).toList();
            authoritiesRepository.saveAllAndFlush(items);
        }

        // 파일 업로드 완료 처리
        uploadDoneService.process(member.getGid());
    }
}