package com.simplescrumpoker.dto.user;

import com.simplescrumpoker.mapper.MappableDto;
import com.simplescrumpoker.validation.EqualFields;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
@FieldNameConstants
@EqualFields(baseField = UserSetPasswordDto.Fields.password,
        matchField = UserSetPasswordDto.Fields.repeatPassword,
        message = "{user.set.repeatPassword.error.notRepeated}")
public class UserSetPasswordDto implements MappableDto {

    @NotBlank(message = "{user.set.password.error.isBlank}")
    @Size(min = 6, max = 100, message = "{user.set.password.error.wrongSize}")
    String password;

    @NotBlank(message = "{user.set.repeatPassword.error.isBlank}")
    String repeatPassword;

}
