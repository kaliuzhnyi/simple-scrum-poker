package com.simplescrumpoker.mapper.retro;

import com.simplescrumpoker.dto.retro.RetroReadDto;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.model.retro.Retro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetroReadMapper implements RetroMapperDto<RetroReadDto> {
    private final UserReadMapper userReadMapper;

    @Override
    public RetroReadDto mapToDto(Retro entity) {
        return RetroReadDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .password(entity.getPassword())
                .status(entity.getStatus())
                .owner(userReadMapper.mapToDto(entity.getOwner()))
                .build();
    }
}
