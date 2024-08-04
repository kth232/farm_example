package com.joyfarm.global.configs;

import com.joyfarm.member.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //일반 웹페이지는 토큰 인증이 필요함, 다른 서버에서 데이터 요청받음
        //같은 출처(서버)에서 요청받은 데이터인지 검증

        //시큐리티 내장 필터가 동작하기 전에 cors 필터를 추가해서 먼저 수행하도록 함, 토큰을 통한 로그인 유지
        http.csrf(c->c.disable()) //토큰 무력화
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //세션 생성 전략 설정, 세션을 사용하는 것이 기본 동작이지만 여기선 세션 자체를 사용하지 않도록 stateless로 설정
                .exceptionHandling(h-> {
                    h.authenticationEntryPoint((req, res, e) -> res.sendError(HttpStatus.UNAUTHORIZED.value())); //rest 형태 서버이기 때문에 주소 이동 없이 응답 코드만 내보냄
                    h.accessDeniedHandler((req, res, e) -> res.sendError(HttpStatus.UNAUTHORIZED.value()));
                })
                .authorizeHttpRequests(c -> {
                    c.requestMatchers(
                            "/account",
                                    "/account/token")
                            .permitAll() //회원가입, 로그인(토큰)은 모든 접근 가능
                            .anyRequest().authenticated(); //그 외에는 인증 필요
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
