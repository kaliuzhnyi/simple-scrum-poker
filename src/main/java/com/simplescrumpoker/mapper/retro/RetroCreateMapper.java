package com.simplescrumpoker.mapper.retro;

import com.simplescrumpoker.dto.retro.RetroCreateDto;
import com.simplescrumpoker.model.retro.Retro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetroCreateMapper implements RetroMapperDto<RetroCreateDto> {

    @Override
    public Retro mapToEntity(RetroCreateDto objectDto) {
        return Retro.builder()
                .title(objectDto.getTitle())
                .description(objectDto.getDescription())
                .password(objectDto.getPassword())
                .build();
    }
}
