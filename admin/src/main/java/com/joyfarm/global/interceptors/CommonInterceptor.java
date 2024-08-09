package com.joyfarm.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CommonInterceptor implements HandlerInterceptor {
    @Override //컨트롤러 수행 전
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //모든 컨트롤러 공통 처리..

        checkDevice(request);

        //모바일 수동 변경 처리 추가 예정
        //기타 관리가 필요한 설정들도 추가 예정

        return true;
    }

    /**
     * PC와 MOBILE 수동 변환
     * ?device=MOBILE
     * ?device=PC
     * @param request
     */
    private void checkDevice(HttpServletRequest request) {
        String device = request.getParameter("device");
        if (!StringUtils.hasText(device)) {
            return; //중첩 반복하는 것보다 끊어서 코드 작성하는 것이 가독성 좋다
        }
        device=device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC";

        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }
}
