package com.kakao.openfeign.external;

import com.kakao.openfeign.dto.response.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoInfoClient", url = "${kakao.user-info-url}")
public interface KakaoUserInfoClient {

    @GetMapping
    KakaoUserInfoResponse kakaoUserInfo(
            @RequestHeader("Authorization") final String token,
            @RequestHeader(name = "Content-type") final String contentType
    );
}
