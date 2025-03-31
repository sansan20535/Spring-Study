package com.kakao.openfeign.controller;


import com.kakao.openfeign.dto.request.KakaoSocialLoginRequest;
import com.kakao.openfeign.dto.request.KakaoUnlinkRequest;
import com.kakao.openfeign.dto.response.KakaoUnlinkResponse;
import com.kakao.openfeign.dto.response.KakaoUserInfoResponse;
import com.kakao.openfeign.service.KakaSocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoSocialLoginController {

    private final KakaSocialLoginService kakaSocialLoginService;

    @PostMapping("/login/openfeign")
    public KakaoUserInfoResponse login(
            @RequestBody KakaoSocialLoginRequest kakaoSocialLoginRequest
    ) {
        return kakaSocialLoginService.login(kakaoSocialLoginRequest.code());
    }

    @PostMapping("/unlink/openfeign")
    public KakaoUnlinkResponse unlink(
            @RequestBody KakaoUnlinkRequest kakaoUnlinkRequest
    ) {
        return kakaSocialLoginService.unlink(kakaoUnlinkRequest.id());
    }
}
