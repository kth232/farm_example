package com.joyfarm.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {
    
    @Value("${cors.allow.origins}") //직접 설정할 수 있게 변수로 정의
    private String allowedOrigins;
    
    //Cors 관련 헤더 - 응답 헤더 추가
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*"); //모든 요청 헤더 허용
        config.addAllowedMethod("*"); //모든 요청 메서드 허용
        config.addAllowedOrigin(allowedOrigins);
        //System.out.println("origins: "+ allowedOrigins);
        //실제로는 보안적으로 안전하도록 상세 설정을 통해 일부만 허용하도록 한정하는 것이 좋다

        source.registerCorsConfiguration("/**", config); //모든 주소에 설정 반영

        return new CorsFilter(source);
    }
}
