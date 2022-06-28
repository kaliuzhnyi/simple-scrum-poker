package com.simplescrumpoker.mapper.room;

import com.simplescrumpoker.dto.room.RoomCreateDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreateMapper implements RoomMapperDto<RoomCreateDto> {

    @Override
    public Room mapToEntity(RoomCreateDto objectDto) {
        return Room.builder()
                .title(objectDto.getTitle())
                .description(objectDto.getDescription())
                .password(objectDto.getPassword())
                .build();
    }
}
