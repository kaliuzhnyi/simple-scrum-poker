package com.simplescrumpoker.validation;

import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class CurrentUserIdValidator implements ConstraintValidator<CurrentUserId, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(UserSecurityDetailsDto.class::cast)
                .map(UserSecurityDetailsDto::getId)
                .map(value::equals)
                .orElse(false);
    }

}
