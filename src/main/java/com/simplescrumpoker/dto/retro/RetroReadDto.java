package com.simplescrumpoker.dto.retro;

import com.simplescrumpoker.dto.room.RoomDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.model.retro.RetroStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RetroReadDto extends RoomDto {

    Long id;
    String title;
    String description;
    String password;
    RetroStatus status;
    UserReadDto owner;

}
