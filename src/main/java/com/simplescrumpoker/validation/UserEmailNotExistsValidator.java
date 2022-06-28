package com.simplescrumpoker.validation;

import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UserEmailNotExistsValidator implements ConstraintValidator<UserEmailNotExists, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value)
                .map(userService::existsByEmail)
                .map(exists -> !exists)
                .orElse(false);
    }
}
