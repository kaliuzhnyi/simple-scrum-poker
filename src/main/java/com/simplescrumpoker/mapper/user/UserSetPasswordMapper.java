package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.dto.user.UserSetPasswordDto;
import com.simplescrumpoker.dto.user.UserUpdatePasswordDto;
import com.simplescrumpoker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSetPasswordMapper implements UserMapperDto<UserSetPasswordDto> {
    private final PasswordEncoder passwordEncoder;
    @Override
    public User copyToEntity(UserSetPasswordDto objectDto, User entity) {
        entity.setPassword(passwordEncoder.encode(objectDto.getPassword()));
        return entity;
    }
}
