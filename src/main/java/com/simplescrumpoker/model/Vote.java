package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "votes")
public class Vote extends AuditableEntity implements MappableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "value")
    private VoteCard value;

    @Column(name = "comment")
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    public void setRoom(Room room) {
        var prevRoom = this.room;
        this.room = room;
        if (Objects.nonNull(room) && !room.getVotes().contains(this)) {
            room.addVote(this);
        }
        if (Objects.nonNull(prevRoom) && prevRoom.getVotes().contains(this)) {
            prevRoom.removeVote(this);
        }
    }

    public void setGuest(Guest guest) {
        var prevGuest = this.guest;
        this.guest = guest;
        if (Objects.nonNull(guest) && !guest.getVotes().contains(this)) {
            guest.addVote(this);
        }
        if (Objects.nonNull(prevGuest) && prevGuest.getVotes().contains(this)) {
            prevGuest.removeVote(this);
        }
    }
}
