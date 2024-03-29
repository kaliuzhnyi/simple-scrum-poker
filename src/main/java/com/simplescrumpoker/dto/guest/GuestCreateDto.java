package com.simplescrumpoker.dto.guest;

import com.simplescrumpoker.model.guest.GuestType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuestCreateDto extends GuestDto {
    String name;
    GuestType type;
    Long userId;
    Long roomId;
    Long retroId;
}
