package com.simplescrumpoker.mapper.retro.message;

import com.simplescrumpoker.dto.retro.message.RetroMessageCreateDto;
import com.simplescrumpoker.model.retro.RetroMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetroMessageCreateMapper implements RetroMessageMapperDto<RetroMessageCreateDto> {

    @Override
    public RetroMessage mapToEntity(RetroMessageCreateDto objectDto) {
        return RetroMessage.builder()
                .value(objectDto.getValue())
                .type(objectDto.getType())
                .build();
    }
}
