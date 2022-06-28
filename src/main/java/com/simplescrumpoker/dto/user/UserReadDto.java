package com.simplescrumpoker.dto.user;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@Builder
@FieldNameConstants
public class UserReadDto extends UserDto {

    Long id;
    String name;
    String email;

}
