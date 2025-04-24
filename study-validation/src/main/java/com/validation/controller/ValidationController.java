package com.validation.controller;

import com.validation.dto.request.ValidationRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/validation")
public class ValidationController {

    @GetMapping
    public String checkValidation(
            @RequestBody @Valid final ValidationRequest validationRequest
    ) {
        return "OK";
    }
}
