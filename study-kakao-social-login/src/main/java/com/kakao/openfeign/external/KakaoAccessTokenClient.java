package com.kakao.openfeign.external;


import com.kakao.openfeign.dto.response.KakaoAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoAccessTokenClient", url = "${kakao.token-url}")
public interface KakaoAccessTokenClient {

    @PostMapping
    KakaoAccessTokenResponse kakaoAccessToken(
            @RequestHeader(name = "Content-type") final String contentType,
            @RequestParam(name = "code") final String code,
            @RequestParam(name = "client_id") final String clientId,
            @RequestParam(name = "redirect_uri") final String redirectUri,
            @RequestParam(name = "grant_type") final String grantType);
}
