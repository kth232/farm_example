package com.joyfarm.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Utils {

    private final DiscoveryClient discoveryClient; //springframework 패키지 걸로 해야 함

    private final HttpServletRequest request;

    public String url(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");

        return String.format("%s%s", instances.get(0).getUri().toString(), url);
        //인스턴스 실제 주소, html 타임리프 레이아웃 링크 주소가 매개변수로 유입됨
        //반환값: admin의 실제 주소(3002) + 정적 경로(url)
    }
    /*
    public String redirectUrl(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances("admin-service");
        String fromGateway = request.getHeader("from-gateway");
        if (StringUtils.hasText("fromGateway") && fromGateway.equals("true")) {
            String host = request.getHeader("gateway-port");
            String protocol = request.isSecure() ? "https://" : "http://";
             url = protocol + host + "/admin" + url;
        }

        return "redirect:" + url;

    }
    */
}
