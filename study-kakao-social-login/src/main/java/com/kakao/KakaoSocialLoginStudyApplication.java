package com.kakao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KakaoSocialLoginStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(KakaoSocialLoginStudyApplication.class, args);
    }
}
