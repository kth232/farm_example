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

    private final JwtProperties properties; // 유효시간을 가지고 오기 위해 사용
    private final AuthenticationManagerBuilder authenticationManagerBuilder; // 토큰을 가지고 승인 정보를 만들어 준다.
    private final MemberInfoService infoService;
    
    /**
     * JWT 토큰 생성
     *
     * @param email
     * @param password
     * @return
     */
    // 토큰 생성하기 (이메일과 패스워드로 토큰 생성)
    public String createToken(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        // 이메일과 패스워드로 검증을 하고, 이상이 없으면 토큰을 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // 로그인 성공 시 -> JWT 토큰을 발급한다. 토큰을 발급하고 제공하는 것이 로그인 절차이다.
            long now = (new Date()).getTime();
            Date validity = new Date(now + properties.getValidSeconds() * 1000);
            // 여기에 지정된 시간만큼 현재 시간에서 유효시간이 설정된다. 환경변수에 따라 충분히 가감이 가능하다.

            return Jwts.builder().setSubject(authentication.getName())
                    .signWith(getKey(), SignatureAlgorithm.HS512)
                    // HMAC + SHA512(Hash를 만들어 주는 것)
                    // .getName() -> 사용자 이메일 가지고 오는 것!
                    // 이곳에서 가지고 오는 이메을은 확실하게 인증된 사용자 이메일. 로그인된 사용자 정보를 가지고 올 때 활용!
                    .expiration(validity)
                    .compact();
            // 만료 시간도 토큰에 포함한다. 그렇기 때문에 따로 저장소에서 관리하지 않아도 된다.
        }
        return null;
    }

    /**
     * 토큰으로 회원 인증 객체 조회
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token){ //로그인한 회원의 인증 정보, 유효한지 검증
        //토큰 검증 먼저-> 예외 통과 시 다음 단계 진행
        validateToken(token);

        // 이상 없으면 이곳으로 유입
        Claims claims = Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token) // 이 토큰에 회원 인증 정보가 들어있다.
                .getPayload();

        String email = claims.getSubject(); //따로 저장 처리 안해도 됨

        MemberInfo memberInfo = (MemberInfo)infoService.loadUserByUsername(email);
        //스프링 시큐리티는 로그인 정보가 있으면 내부적으로 필터를 통해 전역에 로그인 정보 유지함
        //기본 동작은 세션 영역에서, 필터가 로그인 처리하기 전에 토큰을 통해 로그인 유지할 예정

        return new UsernamePasswordAuthenticationToken(memberInfo, token, memberInfo.getAuthorities());
        //회원 정보, 토큰, 권한 정보 반환
    }

    /**
     * 토큰 유효성 검사
     * @param token
     */
    public void validateToken(String token) { //인증한 정보 가져오기<-교체하면 로그인한 상태가 됨
        String errorCode = null;

        try {
            Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getPayload();
            // getPayload() -> 인증 정보를 가지고 온다.
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
            errorCode = "Error.jwt"; //에러 코드 형태로 출력되도록 가공
            e.printStackTrace();
        }
        if (errorCode != null) {
            throw new UnAuthorizedException(errorCode);
        }
        // 토큰 예외 종류에 따라 어떤 예외인지 알 수 있다.
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
        // 여기서 키값으로 검증한다. 데이터가 바뀌어서 키가 바뀌면 검증에 실패한다.
    }
}
