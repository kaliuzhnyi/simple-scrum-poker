package com.simplescrumpoker.validation;

import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomPasswordCorrectValidator implements ConstraintValidator<RoomPasswordCorrect, Object> {
    private String message;
    private String fieldNameId;
    private String fieldNamePassword;

    private final RoomService roomService;

    @Override
    public void initialize(RoomPasswordCorrect constraintAnnotation) {
        message = constraintAnnotation.message();
        fieldNameId = constraintAnnotation.fieldNameId();
        fieldNamePassword = constraintAnnotation.fieldNamePassword();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var id = (Long) getFieldValue(value, fieldNameId);
        var password = (String) getFieldValue(value, fieldNamePassword);

        return Optional.ofNullable(id)
                .map(v -> roomService.passwordCorrect(v, password))
                .or(() -> Optional.of(false))
                .filter(r -> r)
                .or(() -> {
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(fieldNamePassword)
                            .addConstraintViolation();
                    return Optional.empty();
                })
                .isPresent();
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> cls = object.getClass();
        var field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
