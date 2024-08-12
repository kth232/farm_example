package com.joyfarm.member;

import com.joyfarm.member.entities.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
public class MemberInfo implements UserDetails {
    //스프링 시큐리티가 제공하는 user

    private String email;
    private String password;
    private Collection<?extends GrantedAuthority> authorities;
    private Member member;

    //api 서버 쪽에 토큰을 요청하고 토큰을 통해 인증 정보 가져옴
    //서버가 다르기 때문에
    //응답 데이터는 회원 정보 json 문자열 데이터를 받음->자바 객체로 변환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
