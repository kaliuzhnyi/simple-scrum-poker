package com.simplescrumpoker.dto.guest;

import com.simplescrumpoker.model.GuestType;
import com.simplescrumpoker.model.VoteCard;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;

public interface GuestVoteView {

    interface Guest {

        @Value("#{target.id}")
        Long getId();

        @Value("#{target.name}")
        String getName();

        @Value("#{target.type}")
        GuestType getType();

    }

    interface User {

        @Value("#{target.id}")
        Long getId();

        @Value("#{target.name}")
        String getName();

        @Value("#{target.email}")
        String getEmail();
    }

    interface Vote {

        @Value("#{target.id}")
        Long getId();

        @Value("#{target.comment}")
        String getComment();

        @Value("#{target.value}")
        VoteCard getValue();

        @Value("#{target.value.text}")
        String getRepresentation();

        @Value("#{target.lastModifiedDate}")
        Instant getPeriod();

    }

    interface Room {

        @Value("#{target.id}")
        Long getId();
    }

    @Value("#{target.guest}")
    Guest getGuest();

    @Value("#{target.user}")
    User getUser();

    @Value("#{target.vote}")
    Vote getVote();

    @Value("#{target.room}")
    Room getRoom();

    @Value("#{target.user?.id == target.room?.owner?.id}")
    boolean getIsRoomOwner();

    static Comparator<GuestVoteView> comparatorVote() {
        return Comparator.comparing(v -> Optional.ofNullable(v).map(GuestVoteView::getVote).map(Vote::getValue).orElse(null),
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

    static Comparator<GuestVoteView> comparatorVotePeriod() {
        return Comparator.comparing(v -> Optional.ofNullable(v).map(GuestVoteView::getVote).map(Vote::getPeriod).orElse(null),
                Comparator.nullsLast(Comparator.naturalOrder()));
    }

}
