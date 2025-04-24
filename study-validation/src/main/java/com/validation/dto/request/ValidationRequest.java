package com.validation.dto.request;

import com.validation.enums.ValidationType;
import com.validation.validator.ValidationConstraint;

@ValidationConstraint(typeStringValue = "validationType", idStringValue = "id")
public record ValidationRequest(
        ValidationType validationType,
        Long id
) {
}
