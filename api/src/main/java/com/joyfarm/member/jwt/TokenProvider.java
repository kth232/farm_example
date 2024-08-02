package com.joyfarm.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {
    public String createToken() { //토큰 생성

        return null;
    }
    
    public Authentication getAuthentication(String token) { //로그인한 회원의 인증 정보, 유효한지 검증
        return null;
    }

    public void validateToken(String token) { //인증한 정보 가져오기<-교체하면 로그인한 상태가 됨
        
    }
}
