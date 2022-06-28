package com.simplescrumpoker.dto.user;

import com.simplescrumpoker.validation.CurrentUserPassword;
import com.simplescrumpoker.validation.EqualFields;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
@FieldNameConstants
@EqualFields(baseField = UserUpdatePasswordDto.Fields.password,
        matchField = UserUpdatePasswordDto.Fields.repeatPassword,
        message = "{user.set.repeatPassword.error.notRepeated}")
public class UserUpdatePasswordDto extends UserDto {

    @CurrentUserPassword(message = "Current password is invalid")
    String currentPassword;

    @NotBlank(message = "{user.set.password.error.isBlank}")
    @Size(min = 6, max = 100, message = "{user.set.password.error.wrongSize}")
    String password;

    @NotBlank(message = "{user.set.repeatPassword.error.isBlank}")
    String repeatPassword;

}
