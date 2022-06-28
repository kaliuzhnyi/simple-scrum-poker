package com.simplescrumpoker.dto.guest;

import com.simplescrumpoker.dto.user.UserReadDto;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuestReadDto extends GuestDto {
    Long id;
    String name;
    UserReadDto user;
}
