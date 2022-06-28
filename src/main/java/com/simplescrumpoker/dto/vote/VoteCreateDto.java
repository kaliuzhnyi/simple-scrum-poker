package com.simplescrumpoker.dto.vote;

import com.simplescrumpoker.model.VoteCard;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class VoteCreateDto extends VoteDto {

    @NotNull
    VoteCard value;

    @Size(max = 100)
    String comment;
}
