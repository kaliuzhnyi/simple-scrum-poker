package com.simplescrumpoker.dto.guest;

import com.simplescrumpoker.model.GuestType;
import com.simplescrumpoker.model.VoteCard;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;

public interface RoomGuestProjection {
    Long getId();
    String getName();
    String getEmail();
    GuestType getType();
    VoteCard getVote();
    String getVoteComment();
    Instant getVotePeriod();

    default LocalDate getVoteDate() {
        return LocalDate.ofInstant(getVotePeriod(), ZoneId.systemDefault());
    }

    default LocalTime getVoteTime() {
        return LocalTime.ofInstant(getVotePeriod(), ZoneId.systemDefault());
    }

    static Comparator<RoomGuestProjection> comparatorVote() {
        return Comparator.comparing(RoomGuestProjection::getVote,
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

    static Comparator<RoomGuestProjection> comparatorVotePeriod() {
        return Comparator.comparing(RoomGuestProjection::getVotePeriod,
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

}
