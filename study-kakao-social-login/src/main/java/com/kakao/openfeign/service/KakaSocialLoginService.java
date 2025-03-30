package com.kakao.openfeign.service;

import com.kakao.openfeign.dto.response.KakaoAccessTokenResponse;
import com.kakao.openfeign.dto.response.KakaoUserInfoResponse;
import com.kakao.openfeign.external.KakaoAccessTokenClient;
import com.kakao.openfeign.external.KakaoUserInfoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaSocialLoginService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${kakao.content-type}")
    private String kakaoContentType;
    @Value("${kakao.grant-type}")
    private String kakaoGrantType;

    private static final String TOKEN_TYPE = "Bearer ";

    private final KakaoAccessTokenClient kakaoAccessTokenClient;
    private final KakaoUserInfoClient kakaoUserInfoClient;

    public KakaoUserInfoResponse login(final String code) {
        final KakaoAccessTokenResponse kakaoAccessTokenResponse = kakaoAccessTokenClient.kakaoAuth(
                kakaoContentType,
                code,
                kakaoClientId,
                kakaoRedirectUri,
                kakaoGrantType
        );
        return kakaoUserInfoClient.kakaoUserInfo(
                TOKEN_TYPE + kakaoAccessTokenResponse.accessToken(),
                kakaoContentType
        );
    }
}
