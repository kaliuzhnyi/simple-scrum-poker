package com.simplescrumpoker.mapper.room;

import com.simplescrumpoker.dto.room.RoomUpdateDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomUpdateMapper implements RoomMapperDto<RoomUpdateDto> {
    @Override
    public RoomUpdateDto mapToDto(Room entity) {
        return RoomUpdateDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .password(entity.getPassword())
                .build();
    }
    @Override
    public Room copyToEntity(RoomUpdateDto objectDto, Room entity) {
        entity.setTitle(objectDto.getTitle());
        entity.setDescription(objectDto.getDescription());
        entity.setPassword(objectDto.getPassword());
        return entity;
    }
}
