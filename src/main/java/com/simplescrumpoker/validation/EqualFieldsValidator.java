package com.simplescrumpoker.validation;

import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {
    private String message;
    private String baseFieldName;
    private String matchFieldName;

    @Override
    public void initialize(EqualFields constraintAnnotation) {
        message = constraintAnnotation.message();
        baseFieldName = constraintAnnotation.baseField();
        matchFieldName = constraintAnnotation.matchField();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        var baseFieldValue = getFieldValue(value, baseFieldName);
        var matchFieldValue = getFieldValue(value, matchFieldName);
        if (Objects.isNull(baseFieldValue) && Objects.isNull(matchFieldValue)) {
            return true;
        }

        if (Objects.isNull(baseFieldValue) || !baseFieldValue.equals(matchFieldValue)) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(matchFieldName)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> cls = object.getClass();
        var field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
