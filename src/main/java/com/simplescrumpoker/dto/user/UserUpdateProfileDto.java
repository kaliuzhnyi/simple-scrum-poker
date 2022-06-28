package com.simplescrumpoker.dto.user;

import com.simplescrumpoker.mapper.MappableDto;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class UserUpdateProfileDto extends UserDto {

    Long id;

    @NotBlank(message = "{user.signup.name.error.isBlank}")
    @Size(min = 3, max = 50, message = "{user.signup.name.error.wrongSize}")
    String name;

    String email;

}
