package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "guests")
public class Guest extends BaseEntity implements MappableEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GuestType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "guests_users",
            joinColumns = @JoinColumn(name = "guest_id", referencedColumnName = "id", nullable = false, unique = true),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private List<GuestRoom> guestRooms = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    public void setUser(User user) {

        if (Objects.nonNull(this.user) && Objects.equals(this.user.getGuest(), this)) {
            this.user.setGuest(null);
        }

        this.user = user;
        if (Objects.nonNull(user) && !Objects.equals(user.getGuest(), this)) {
            user.setGuest(this);
        }

        this.type = Objects.nonNull(user) ? GuestType.USER : GuestType.ANONYMOUS;

    }

    public void addVote(Vote vote) {
        votes.add(vote);
        if (!this.equals(vote.getGuest())) {
            vote.setGuest(this);
        }
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
        if (this.equals(vote.getGuest())) {
            vote.setGuest(null);
        }
    }

    public void addRoomGuest(GuestRoom guestRoom) {
        guestRooms.add(guestRoom);
    }

    public void addRoomGuest(Room room) {
        GuestRoom guestRoom = GuestRoom.builder()
                .guest(this)
                .room(room)
                .accessStatus(true)
                .build();
        addRoomGuest(guestRoom);
    }

    public void removeRoomGuest(GuestRoom guestRoom) {
        guestRooms.remove(guestRoom);
    }

}
