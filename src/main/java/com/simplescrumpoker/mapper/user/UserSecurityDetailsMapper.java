package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityDetailsMapper implements UserMapperDto<UserSecurityDetailsDto> {

    @Override
    public UserSecurityDetailsDto mapToDto(User entity) {
        return UserSecurityDetailsDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .build();
    }
}
