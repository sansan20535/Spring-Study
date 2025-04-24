package com.validation.validator;

import com.validation.enums.ValidationType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ValidationValidator implements ConstraintValidator<ValidationConstraint, Object> {

    private String validationTypeStringValue;
    private String idStringValue;

    @Override
    public void initialize(final ValidationConstraint validationConstraint) {
        validationTypeStringValue = validationConstraint.typeStringValue();
        idStringValue = validationConstraint.idStringValue();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext constraintValidatorContext) {
        final ValidationType validationType = getValidationType(object);
        final Long id = getIdValue(object);
        if (validationType == ValidationType.STUDY1) {
            throw new RuntimeException("validationType is STUDY1");
        }
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        if (id < 1) {
            throw new RuntimeException("invalid id");
        }
        return true;
    }

    private ValidationType getValidationType(final Object object) {
        return (ValidationType) getFieldValue(object, validationTypeStringValue);
    }

    private Long getIdValue(final Object object) {
        return (Long) getFieldValue(object, idStringValue);
    }

    // 리플렉션을 이용하여 필드를 가져오는 부분
    private Object getFieldValue(final Object object, final String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            return dateField.get(object);
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new RuntimeException();
    }
}
