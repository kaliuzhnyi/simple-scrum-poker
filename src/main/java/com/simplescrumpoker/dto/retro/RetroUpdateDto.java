package com.simplescrumpoker.dto.retro;

import com.simplescrumpoker.model.retro.RetroStatus;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class RetroUpdateDto extends RetroDto {

    Long id;

    @NotBlank(message = "{retro.set.error.title.empty}")
    @Size(min = 3, max = 50, message = "{retro.set.error.title.wrongSize}")
    String title;

    @Size(max = 1000, message = "{retro.set.error.description.wrongSize}")
    String description;

    @NotBlank(message = "{retro.set.error.password.empty}")
    @Size(min = 4, max = 100, message = "{retro.set.error.password.wrongSize}")
    String password;

    @NotNull(message = "{retro.set.error.status.empty}")
    RetroStatus status;

}
