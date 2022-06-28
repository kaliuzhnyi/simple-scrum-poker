package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"guestRooms", "votes"})
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

//    @ToString.Exclude
//    @Builder.Default
//    @ManyToMany(mappedBy = "rooms", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Guest> guests = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<GuestRoom> guestRooms = new ArrayList<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

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

    public void addRoomGuest(GuestRoom guestRoom) {
        guestRooms.add(guestRoom);
    }

    public void addRoomGuest(Guest guest) {
        GuestRoom guestRoom = GuestRoom.builder()
                .guest(guest)
                .room(this)
                .accessStatus(true)
                .build();
        addRoomGuest(guestRoom);
    }

    public void removeRoomGuest(GuestRoom guestRoom) {
        guestRooms.remove(guestRoom);
    }

//    public void addGuest(Guest guest) {
//        guests.add(guest);
//        if (!guest.getRooms().contains(this)) {
//            guest.addRoom(this);
//        }
//    }
//    public void removeGuest(Guest guest) {
//        guests.remove(guest);
//        if (guest.getRooms().contains(this)) {
//            guest.removeRoom(this);
//        }
//    }

}
