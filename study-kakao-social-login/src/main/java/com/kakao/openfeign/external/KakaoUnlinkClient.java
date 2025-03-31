package com.kakao.openfeign.external;

import com.kakao.openfeign.dto.response.KakaoUnlinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakaoUnlinkClient", url = "${kakao.unlink-url}")
public interface KakaoUnlinkClient {

    @PostMapping
    KakaoUnlinkResponse kakaoUnlink(
            @RequestHeader(name = "Authorization") final String adminKey,
            @RequestHeader(name = "Content-type") final String contentType,
            @RequestParam(name = "target_id_type") final String targetIdType,
            @RequestParam(name = "target_id") final Long targetId);
}
