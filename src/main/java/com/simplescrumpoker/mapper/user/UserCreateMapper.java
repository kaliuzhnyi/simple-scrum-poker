package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.dto.user.UserCreateDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements UserMapperDto<UserCreateDto> {
    private final PasswordEncoder passwordEncoder;
    @Override
    public User mapToEntity(UserCreateDto objectDto) {
        return User.builder()
                .name(objectDto.getName())
                .email(objectDto.getEmail())
                .password(passwordEncoder.encode(objectDto.getPassword()))
                .build();
    }
}
