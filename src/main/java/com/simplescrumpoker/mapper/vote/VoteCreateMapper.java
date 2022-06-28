package com.simplescrumpoker.mapper.vote;

import com.simplescrumpoker.dto.vote.VoteCreateDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteCreateMapper implements VoteMapperDto<VoteCreateDto> {
    @Override
    public Vote mapToEntity(VoteCreateDto objectDto) {
        return Vote.builder()
                .value(objectDto.getValue())
                .comment(objectDto.getComment())
                .build();
    }

    @Override
    public Vote copyToEntity(VoteCreateDto objectDto, Vote entity) {
        entity.setValue(objectDto.getValue());
        entity.setComment(objectDto.getComment());
        return entity;
    }
}
