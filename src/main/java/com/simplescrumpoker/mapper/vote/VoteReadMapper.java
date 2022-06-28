package com.simplescrumpoker.mapper.vote;

import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.model.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteReadMapper implements VoteMapperDto<VoteReadDto> {

    private final UserReadMapper userReadMapper;

    @Override
    public VoteReadDto mapToDto(Vote entity) {
        return VoteReadDto.builder()
                .value(entity.getValue())
                .comment(entity.getComment())
                //.user(userReadMapper.mapToDtoFor(entity.getUser(), VoteReadDto.class))
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
