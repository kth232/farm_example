package com.joyfarm.global.configs;

import com.joyfarm.global.interceptors.CommonInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    //사이트 공통 인터셉터
    private final CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //공통 적용일 경우 addPathPatterns 없어도 됨
        registry.addInterceptor(commonInterceptor);
                //.addPathPatterns("/**"); //전체
    }
}
