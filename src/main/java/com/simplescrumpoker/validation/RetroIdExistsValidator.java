package com.simplescrumpoker.validation;

import com.simplescrumpoker.service.RetroService;
import com.simplescrumpoker.service.RoomService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class RetroIdExistsValidator implements ConstraintValidator<RetroIdExists, Long> {
    private final RetroService retroService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(retroService::exists)
                .orElse(false);
    }
}
