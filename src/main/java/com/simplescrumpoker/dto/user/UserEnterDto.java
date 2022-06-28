package com.simplescrumpoker.dto.user;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@RequiredArgsConstructor
public class UserEnterDto extends UserDto {

    @NotBlank(message = "{user.signin.email.error.isBlank}")
    @Email(message = "{user.signin.email.error.invalid}")
    String email;

    @NotBlank(message = "{user.signin.password.error.isBlank}")
    String password;

}
