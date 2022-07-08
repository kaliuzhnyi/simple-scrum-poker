package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Objects;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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

    public VoteCard getValue() {
        return this.value;
    }

    public String getComment() {
        return this.comment;
    }

    public Room getRoom() {
        return this.room;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public void setValue(VoteCard value) {
        this.value = value;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Vote)) return false;
        final Vote other = (Vote) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        final Object this$room = this.getRoom();
        final Object other$room = other.getRoom();
        if (this$room == null ? other$room != null : !this$room.equals(other$room)) return false;
        final Object this$guest = this.getGuest();
        final Object other$guest = other.getGuest();
        if (this$guest == null ? other$guest != null : !this$guest.equals(other$guest)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Vote;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        final Object $room = this.getRoom();
        result = result * PRIME + ($room == null ? 43 : $room.hashCode());
        final Object $guest = this.getGuest();
        result = result * PRIME + ($guest == null ? 43 : $guest.hashCode());
        return result;
    }

    public String toString() {
        return "Vote(value=" + this.getValue() + ", comment=" + this.getComment() + ", room=" + this.getRoom() + ", guest=" + this.getGuest() + ")";
    }
}
