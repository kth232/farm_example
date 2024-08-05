package com.joyfarm.member.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// 스프링 시큐리티 내에서 사용하는 필터는 별도의 방식이 있다. 여기서 정의하고 추가하면 됨
// 스프링 시큐리티에서는 필터 전, 후 등 특정 시점에 추가할 수 있는 기능이 있음
// 그 점을 활용해서 토큰으로 로그인을 하고 로그인을 유지하는 방식을 기본 필터 전에 먼저 추가할 것임
public class JwtFilter extends GenericFilterBean { //직접 필터를 정의할 수 있는 클래스

    private final TokenProvider provider;

    /**
     * 요청 헤더 Authorization: Bearer JWT토큰 값, 해당 값만 추출해서 토큰 전달
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //시점 별로 필터 수행 가능

        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            //토큰으로 회원 인증 객체 -> 로그인 유지 처리
            Authentication authentication = provider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * 요청 헤더에서 JWT 토큰 추출
     * 요청 헤더 Authorization: Bearer JWT 토큰 값
     * @param request
     * @return
     */
    private String getToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String bearerToken = req.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.toUpperCase().startsWith("BEARER ")) {
            //bearer 로 시작하는 토큰의 값을 추출
            return bearerToken.substring(7).trim(); //공백 제거 후 7번째 자리부터
        }
        return null;
    }
}
