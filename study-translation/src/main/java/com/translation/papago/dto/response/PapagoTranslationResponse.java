package com.translation.papago.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PapagoTranslationResponse(
        Message message
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(
            Result result
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            String srcLangType,
            String tarLangType,
            String translatedText
    ) {
    }
}
