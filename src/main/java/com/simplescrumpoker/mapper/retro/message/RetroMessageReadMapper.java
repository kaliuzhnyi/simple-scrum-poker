package com.simplescrumpoker.mapper.retro.message;

import com.simplescrumpoker.dto.retro.message.RetroMessageReadDto;
import com.simplescrumpoker.mapper.guest.GuestReadMapper;
import com.simplescrumpoker.model.retro.RetroMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetroMessageReadMapper implements RetroMessageMapperDto<RetroMessageReadDto> {

    private final GuestReadMapper guestReadMapper;

    @Override
    public RetroMessageReadDto mapToDto(RetroMessage entity) {
        return RetroMessageReadDto.builder()
                .id(entity.getId())
                .value(entity.getValue())
                .type(entity.getType())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .guest(guestReadMapper.mapToDto(entity.getGuest()))
                .likesCount(entity.getLikes().size())
                .build();
    }
}
