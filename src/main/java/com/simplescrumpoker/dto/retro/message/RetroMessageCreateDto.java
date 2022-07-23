package com.simplescrumpoker.dto.retro.message;

import com.simplescrumpoker.model.retro.RetroMessageType;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class RetroMessageCreateDto extends RetroMessageDto {

    @NotEmpty(message = "{retro.message.set.error.value.empty}")
    @Size(min = 3, max = 250, message = "{retro.message.set.error.value.size}")
    String value;

    @NotNull(message = "{retro.message.set.error.type.empty}")
    RetroMessageType type;
}
