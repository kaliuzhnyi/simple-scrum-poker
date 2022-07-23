package com.simplescrumpoker.dto.retro.message;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.model.retro.RetroMessageType;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class RetroMessageReadDto extends RetroMessageDto {
    Long id;
    String value;
    RetroMessageType type;
    Instant createdDate;
    Instant lastModifiedDate;
    GuestReadDto guest;
    Integer likesCount;
}
