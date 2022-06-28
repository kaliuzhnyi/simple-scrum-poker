package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.dto.user.UserUpdateProfileDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateProfileMapper implements UserMapperDto<UserUpdateProfileDto> {
    @Override
    public UserUpdateProfileDto mapToDto(User entity) {
        return UserUpdateProfileDto.builder()
                .name(entity.getName())
                .build();
    }
    @Override
    public User copyToEntity(UserUpdateProfileDto objectDto, User entity) {
        entity.setName(objectDto.getName());
        return entity;
    }
}
