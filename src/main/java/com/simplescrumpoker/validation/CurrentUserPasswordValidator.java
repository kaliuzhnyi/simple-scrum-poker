package com.simplescrumpoker.validation;

import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.model.User;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class CurrentUserPasswordValidator implements ConstraintValidator<CurrentUserPassword, String> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(UserSecurityDetailsDto.class::cast)
                .map(UserSecurityDetailsDto::getId)
                .flatMap(userRepository::findById)
                .map(User::getPassword)
                .map(currentPassword -> passwordEncoder.matches(value, currentPassword))
                .orElse(false);
    }
}
