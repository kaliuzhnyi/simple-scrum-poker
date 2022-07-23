package com.simplescrumpoker.mapper.retro;

import com.simplescrumpoker.dto.retro.RetroUpdateDto;
import com.simplescrumpoker.model.retro.Retro;
import org.springframework.stereotype.Component;

@Component
public class RetroUpdateMapper implements RetroMapperDto<RetroUpdateDto> {
    @Override
    public Retro copyToEntity(RetroUpdateDto objectDto, Retro entity) {
        entity.setTitle(objectDto.getTitle());
        entity.setDescription(objectDto.getDescription());
        entity.setPassword(objectDto.getPassword());
        entity.setStatus(objectDto.getStatus());
        return entity;
    }
}
