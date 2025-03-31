package com.kakao.openfeign.service;

import com.kakao.openfeign.dto.response.KakaoAccessTokenResponse;
import com.kakao.openfeign.dto.response.KakaoUnlinkResponse;
import com.kakao.openfeign.dto.response.KakaoUserInfoResponse;
import com.kakao.openfeign.external.KakaoAccessTokenClient;
import com.kakao.openfeign.external.KakaoUnlinkClient;
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
    @Value("${kakao.target-id-type}")
    private String targetIdType;
    @Value("${kakao.admin-key}")
    private String adminKey;

    private static final String TOKEN_TYPE = "Bearer ";
    private static final String ADMIN_KEY_TYPE = "KakaoAK ";

    private final KakaoAccessTokenClient kakaoAccessTokenClient;
    private final KakaoUserInfoClient kakaoUserInfoClient;
    private final KakaoUnlinkClient kakaoUnlinkClient;

    public KakaoUserInfoResponse login(final String code) {
        final KakaoAccessTokenResponse kakaoAccessTokenResponse = kakaoAccessTokenClient.kakaoAccessToken(
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

    public KakaoUnlinkResponse unlink(final Long id) {
        return kakaoUnlinkClient.kakaoUnlink(
                ADMIN_KEY_TYPE + adminKey,
                kakaoContentType,
                targetIdType,
                id
        );
    }
}
