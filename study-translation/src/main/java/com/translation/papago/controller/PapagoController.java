package com.translation.papago.controller;

import com.translation.papago.dto.response.PapagoTranslationResponse;
import com.translation.papago.service.PapagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translation")
@RequiredArgsConstructor
public class PapagoController {

    private final PapagoService papagoService;

    @GetMapping("/papago")
    public ResponseEntity<PapagoTranslationResponse> translationByDeepl(
            @RequestParam(value = "source") String source,
            @RequestParam(value = "target") String target,
            @RequestParam(value = "text") String text
    ) {
        return ResponseEntity.ok(papagoService.translation(source, target, text));
    }
}
