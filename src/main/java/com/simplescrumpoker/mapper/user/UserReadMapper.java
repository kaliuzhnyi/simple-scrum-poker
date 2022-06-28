package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.vote.VoteDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Spliterator;

@Component
public class UserReadMapper implements UserMapperDto<UserReadDto> {
    @Override
    public UserReadDto mapToDto(User entity) {
        return UserReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
    public UserReadDto mapToDtoFor(User entity, Class<VoteReadDto> cls) {
        return UserReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
