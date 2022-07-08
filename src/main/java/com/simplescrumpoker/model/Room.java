package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "rooms")
public class Room extends AuditableEntity implements MappableEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "password")
    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestRoom> guestRooms = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();


    public void setOwner(User owner) {

        if (Objects.nonNull(this.owner) && this.owner.getRooms().contains(this)) {
            this.owner.removeRoom(this);
        }

        this.owner = owner;
        if (Objects.nonNull(owner) && !owner.getRooms().contains(this)) {
            owner.addRoom(this);
        }

    }


    public void addVote(Vote vote) {
        votes.add(vote);
        if (!this.equals(vote.getRoom())) {
            vote.setRoom(this);
        }
    }

    public void removeVote(Vote vote) {
        this.votes.remove(vote);
        if (this.equals(vote.getRoom())) {
            vote.setRoom(null);
        }
    }


    public void addRoomGuest(Guest guest) {
        GuestRoom guestRoom = GuestRoom.builder()
                .guest(guest)
                .room(this)
                .accessStatus(true)
                .accessDate(Instant.now())
                .build();
        addRoomGuest(guestRoom);
    }

    public void addRoomGuest(GuestRoom guestRoom) {
        guestRooms.add(guestRoom);

        if (Objects.nonNull(guestRoom) && !Objects.equals(guestRoom.getRoom(), this)) {
            guestRoom.setRoom(this);
        }
    }

    public void removeRoomGuest(GuestRoom guestRoom) {
        guestRooms.remove(guestRoom);

        if (Objects.nonNull(guestRoom) && Objects.equals(guestRoom.getRoom(), this)) {
            guestRoom.setRoom(null);
        }
    }

}
