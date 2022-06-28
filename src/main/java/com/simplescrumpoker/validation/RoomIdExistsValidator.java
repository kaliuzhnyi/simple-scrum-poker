package com.simplescrumpoker.validation;

import com.simplescrumpoker.service.RoomService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomIdExistsValidator implements ConstraintValidator<RoomIdExists, Long> {
    private final RoomService roomService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(roomService::exists)
                .orElse(false);
    }
}
