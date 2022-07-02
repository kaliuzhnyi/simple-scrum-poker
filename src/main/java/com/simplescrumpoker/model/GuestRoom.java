package com.simplescrumpoker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "guests_rooms")
public class GuestRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder.Default
    @Column(name = "access_date", nullable = false)
    private Instant accessDate = Instant.now();

    @Builder.Default
    @Column(name = "access_status")
    private Boolean accessStatus = false;

    public void setGuest(Guest guest) {

        if (Objects.nonNull(this.guest) && this.guest.getGuestRooms().contains(this)) {
            this.guest.removeRoomGuest(this);
        }

        this.guest = guest;
        if (Objects.nonNull(guest) && !guest.getGuestRooms().contains(this)) {
            guest.addRoomGuest(this);
        }

    }

    public void setRoom(Room room) {

        if (Objects.nonNull(this.room) && this.room.getGuestRooms().contains(this)) {
            this.room.removeRoomGuest(this);
        }

        this.room = room;
        if (Objects.nonNull(room) && !room.getGuestRooms().contains(this)) {
            room.addRoomGuest(this);
        }

    }

}
