package com.simplescrumpoker.dto.user;

import com.simplescrumpoker.validation.EqualFields;
import com.simplescrumpoker.validation.UserEmailNotExists;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
@FieldNameConstants
@EqualFields(baseField = UserCreateDto.Fields.password,
        matchField = UserCreateDto.Fields.repeatPassword,
        message = "{user.signup.repeatPassword.error.notRepeated}")
public class UserCreateDto extends UserDto {

    @NotBlank(message = "{user.signup.name.error.isBlank}")
    @Size(min = 3, max = 50, message = "{user.signup.name.error.wrongSize}")
    String name;

    @NotBlank(message = "{user.signup.email.error.isBlank}")
    @Email(message = "{user.signup.email.error.invalid}")
    @Size(max = 100, message = "{user.signup.email.error.wrongSize}")
    @UserEmailNotExists(message = "{user.signup.email.error.exists}")
    String email;

    @NotBlank(message = "{user.signup.password.error.isBlank}")
    @Size(min = 6, max = 100, message = "{user.signup.password.error.wrongSize}")
    String password;

    @NotBlank(message = "{user.signup.repeatPassword.error.isBlank}")
    String repeatPassword;

}

