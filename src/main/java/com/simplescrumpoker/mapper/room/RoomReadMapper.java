package com.simplescrumpoker.mapper.room;

import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.mapper.vote.VoteReadMapper;
import com.simplescrumpoker.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomReadMapper implements RoomMapperDto<RoomReadDto> {
    private final UserReadMapper userReadMapper;
    private final VoteReadMapper voteReadMapper;
    @Override
    public RoomReadDto mapToDto(Room entity) {
        return RoomReadDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .password(entity.getPassword())
                .owner(userReadMapper.mapToDto(entity.getOwner()))
                .build();
    }
}
