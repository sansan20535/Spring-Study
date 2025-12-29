package com.translation.papago.service;

import com.translation.papago.dto.response.PapagoTranslationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class PapagoService {

    @Value("${papago.client-id}")
    private String clientId;

    @Value("${papago.client-secret}")
    private String clientSecret;

    @Value("${papago.translation-url}")
    private String translationUrl;

    public PapagoTranslationResponse translation(String source, String target, String text) {
        RestClient restClient = RestClient.builder()
                .baseUrl(translationUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8")
                .defaultHeader("x-ncp-apigw-api-key-id", clientId)
                .defaultHeader("x-ncp-apigw-api-key", clientSecret)
                .build();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("source", source);
        form.add("target", target);
        form.add("text", text);

        return restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(PapagoTranslationResponse.class);

    }
}
