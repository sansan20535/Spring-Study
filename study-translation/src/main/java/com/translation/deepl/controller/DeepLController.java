package com.translation.deepl.controller;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.translation.deepl.service.DeepLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translation")
@RequiredArgsConstructor
public class DeepLController {

    private final DeepLService deepLService;

    @GetMapping("/deepl")
    public ResponseEntity<TextResult> translationByDeepl(
            @RequestParam(value = "originalText") String originalText,
            @RequestParam(value = "sourceLanguage", required = false) String sourceLanguage,
            @RequestParam(value = "targetLanguage") String targetLanguage
    ) throws DeepLException, InterruptedException {
        return ResponseEntity.ok(deepLService.translation(originalText, sourceLanguage, targetLanguage));
    }
}
