package com.simplescrumpoker.dto.room;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class RoomUpdateDto extends RoomDto {

    Long id;

    @NotBlank(message = "{room.create.title.error.isBlank}")
    @Size(min = 3, max = 50, message = "{room.create.title.error.wrongSize}")
    String title;
    String description;

    @NotBlank(message = "{room.create.password.error.isBlank}")
    @Size(min = 4, max = 25, message = "{room.create.password.error.wrongSize}")
    String password;

}
