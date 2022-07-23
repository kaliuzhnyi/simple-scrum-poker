package com.simplescrumpoker.dto.retro;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class RetroCreateDto extends RetroDto {

    @NotBlank(message = "{retro.set.error.title.empty}")
    @Size(min = 3, max = 50, message = "{retro.set.error.title.size}")
    String title;

    @Size(max = 1000, message = "{retro.set.error.description.size}")
    String description;

    @NotBlank(message = "{retro.set.error.password.empty}")
    @Size(min = 4, max = 100, message = "{retro.set.error.password.size}")
    String password;

}
