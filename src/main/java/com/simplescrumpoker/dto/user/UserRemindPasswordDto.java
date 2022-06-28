package com.simplescrumpoker.dto.user;

import com.simplescrumpoker.validation.UserEmailExists;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
public class UserRemindPasswordDto {

    @NotBlank(message = "{user.remind.password.email.error.isBlank}")
    @Email(message = "{user.remind.password.email.error.invalid}")
    @UserEmailExists(message = "{user.remind.password.email.error.notExists}")
    String email;

}
