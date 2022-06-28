package com.simplescrumpoker.dto.vote;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.model.VoteCard;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class VoteReadDto extends VoteDto implements Comparable<VoteReadDto> {

    VoteCard value;
    String comment;
    Instant createdDate;
    Instant lastModifiedDate;

    GuestReadDto guest;

    @Override
    public int compareTo(VoteReadDto o) {
        return this.value.compareTo(o.getValue());
    }
}
