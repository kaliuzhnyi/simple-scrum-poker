package com.simplescrumpoker.mapper.guest;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.model.guest.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuestReadMapper implements GuestMapperDto<GuestReadDto> {
    private final UserReadMapper userReadMapper;

    @Override
    public GuestReadDto mapToDto(Guest entity) {

        var user = Optional.ofNullable(entity.getUser())
                .map(userReadMapper::mapToDto)
                .orElse(null);

        return GuestReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .user(user)
                .build();
    }
}
