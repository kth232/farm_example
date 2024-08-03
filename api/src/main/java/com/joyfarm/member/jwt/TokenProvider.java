package com.joyfarm.member.jwt;

import com.joyfarm.global.exceptions.UnAuthorizedException;
import com.joyfarm.member.MemberInfo;
import com.joyfarm.member.services.MemberInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class TokenProvider {

    private final JwtProperties properties;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberInfoService infoService;
    
    /**
     * JWT 토큰 생성
     *
     * @param email
     * @param password
     * @return
     */
    public String createToken(String email, String password) { //토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) { // 로그인 성공시 -> JWT 토큰 발급
            long now = (new Date()).getTime();
            Date validity = new Date(now + properties.getValidSeconds() * 1000);

            return Jwts.builder()
                    .setSubject(authentication.getName()) // 사용자 email
                    .signWith(getKey(), SignatureAlgorithm.HS512) // HMAC + SHA512
                    .expiration(validity)
                    .compact();
        }
        return null;
    }

    /**
     * 토큰으로 회원 인증 객체 조회
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) { //로그인한 회원의 인증 정보, 유효한지 검증
        //토큰 검증 먼저-> 예외 통과 시 다음 단계 진행
        validateToken(token);

        Claims claims  = Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();

        String email = claims.getSubject(); //따로 저장 처리 안해도 됨

        MemberInfo memberInfo = (MemberInfo) infoService.loadUserByUsername(email);
        //스프링 시큐리티는 로그인 정보가 있으면 내부적으로 필터를 통해 전역에 로그인 정보 유지함
        //기본 동작은 세션 영역에서, 필터가 로그인 처리하기 전에 토큰을 통해 로그인 유지할 예정

        return new UsernamePasswordAuthenticationToken(memberInfo, token, memberInfo.getAuthorities());
        //회원 정보, 토큰, 권한 정보 반환
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token
     */
    public void validateToken(String token) { //인증한 정보 가져오기<-교체하면 로그인한 상태가 됨
        String errorCode = null;

        try {
            Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getPayload();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            // 변조된 JWT 토큰

            errorCode = "Malformed.jwt";
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            // 유효시간이 만료된 JWT 토큰

            errorCode = "Expired.jwt";
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 형식의 JWT 토큰

            errorCode = "Unsupported.jwt";
            e.printStackTrace();
        } catch (Exception e) {

            errorCode= "Error.jwt"; //에러코드 형태로 출력되도록 가공함
        }

        if (errorCode != null) {
            throw new UnAuthorizedException(errorCode);
        }
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
