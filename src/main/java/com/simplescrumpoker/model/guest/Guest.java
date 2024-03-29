package com.simplescrumpoker.model.guest;

import com.simplescrumpoker.mapper.MappableEntity;
import com.simplescrumpoker.model.BaseEntity;
import com.simplescrumpoker.model.Room;
import com.simplescrumpoker.model.User;
import com.simplescrumpoker.model.Vote;
import com.simplescrumpoker.model.retro.Retro;
import com.simplescrumpoker.model.retro.RetroMessage;
import com.simplescrumpoker.model.retro.RetroMessageLike;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
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
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true))
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestRoom> guestRooms = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestRetro> guestRetros = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RetroMessage> retroMessages = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RetroMessageLike> retroMessageLikes = new ArrayList<>();


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


    public void addRoomGuest(Room room) {
        GuestRoom guestRoom = GuestRoom.builder()
                .guest(this)
                .room(room)
                .accessStatus(true)
                .accessDate(Instant.now())
                .build();
        addRoomGuest(guestRoom);
    }

    public void addRoomGuest(GuestRoom guestRoom) {
        guestRooms.add(guestRoom);

        if (Objects.nonNull(guestRoom) && !Objects.equals(guestRoom.getGuest(), this)) {
            guestRoom.setGuest(this);
        }
    }

    public void removeRoomGuest(GuestRoom guestRoom) {
        guestRooms.remove(guestRoom);

        if (Objects.nonNull(guestRoom) && Objects.equals(guestRoom.getGuest(), this)) {
            guestRoom.setGuest(null);
        }
    }


    public void addRetroGuest(Retro retro) {
        GuestRetro guestRetro = GuestRetro.builder()
                .guest(this)
                .retro(retro)
                .accessStatus(true)
                .accessDate(Instant.now())
                .build();
        addRetroGuest(guestRetro);
    }

    public void addRetroGuest(GuestRetro guestRetro) {
        guestRetros.add(guestRetro);

        if (Objects.nonNull(guestRetro) && !Objects.equals(guestRetro.getGuest(), this)) {
            guestRetro.setGuest(this);
        }
    }

    public void removeRetroGuest(GuestRetro guestRetro) {
        guestRetros.remove(guestRetro);

        if (Objects.nonNull(guestRetro) && Objects.equals(guestRetro.getGuest(), this)) {
            guestRetro.setGuest(null);
        }
    }


    public void addRetroMessage(RetroMessage retroMessage) {
        retroMessages.add(retroMessage);

        if (Objects.nonNull(retroMessage) && !Objects.equals(retroMessage.getGuest(), this)) {
            retroMessage.setGuest(this);
        }
    }

    public void removeRetroMessage(RetroMessage retroMessage) {
        retroMessages.remove(retroMessage);

        if (Objects.nonNull(retroMessage) && Objects.equals(retroMessage.getGuest(), this)) {
            retroMessage.setGuest(null);
        }
    }


    public void addRetroMessageLike(RetroMessageLike like) {
        retroMessageLikes.add(like);

        if (Objects.nonNull(like) && !Objects.equals(like.getGuest(), this)) {
            like.setGuest(this);
        }
    }

    public void removeRetroMessageLike(RetroMessageLike like) {
        retroMessageLikes.remove(like);

        if (Objects.nonNull(like) && Objects.equals(like.getGuest(), this)) {
            like.setGuest(null);
        }
    }

}
