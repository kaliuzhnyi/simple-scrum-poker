package com.simplescrumpoker.mapper.guest;

import com.simplescrumpoker.dto.guest.GuestCreateDto;
import com.simplescrumpoker.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestCreateMapper implements GuestMapperDto<GuestCreateDto> {
    @Override
    public Guest mapToEntity(GuestCreateDto objectDto) {
        return Guest.builder()
                .name(objectDto.getName())
                .type(objectDto.getType())
                .build();
    }
}
