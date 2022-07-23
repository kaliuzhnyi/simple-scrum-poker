package com.simplescrumpoker.dto.retro;

import com.simplescrumpoker.dto.room.RoomDto;
import com.simplescrumpoker.validation.RetroIdExists;
import com.simplescrumpoker.validation.RetroPasswordCorrect;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@RetroPasswordCorrect(message = "{retro.enter.error.password.correct}")
public class RetroEnterDto extends RoomDto {

    @NotNull(message = "{retro.enter.error.id.empty}")
    @RetroIdExists(message = "{retro.enter.error.id.exist}")
    @NonFinal @Setter
    Long id;

    @NotBlank(message = "{retro.enter.error.password.empty}")
    String password;

    @NotBlank(message = "{retro.enter.error.guestName.empty}")
    @NonFinal @Setter
    String guestName;

}
