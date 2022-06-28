package com.simplescrumpoker.dto.room;

import com.simplescrumpoker.validation.RoomIdExists;
import com.simplescrumpoker.validation.RoomPasswordCorrect;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@RoomPasswordCorrect(message = "{room.enter.error.password.correct}")
public class RoomEnterDto extends RoomDto {

    @NotNull(message = "{room.enter.error.id.empty}")
    @RoomIdExists(message = "{room.enter.error.id.exist}")
    @NonFinal @Setter
    Long id;

    @NotBlank(message = "{room.enter.error.password.empty}")
    String password;

    @NotBlank(message = "{room.enter.error.guestName.empty}")
    @NonFinal @Setter
    String guestName;

}
