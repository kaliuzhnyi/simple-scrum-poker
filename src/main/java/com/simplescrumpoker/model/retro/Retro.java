package com.simplescrumpoker.model.retro;

import com.simplescrumpoker.mapper.MappableEntity;
import com.simplescrumpoker.model.AuditableEntity;
import com.simplescrumpoker.model.guest.Guest;
import com.simplescrumpoker.model.guest.GuestRetro;
import com.simplescrumpoker.model.User;
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
@Table(name = "retros")
public class Retro extends AuditableEntity implements MappableEntity {

    @Column(name = "title", nullable = false, length = 50)
    String title;

    @Column(name = "description", length = 1000)
    String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RetroStatus status = RetroStatus.OPEN;

    @Column(name = "password", nullable = false, length = 100)
    String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "retro", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestRetro> guestRetros = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "retro", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetroMessage> retroMessages = new ArrayList<>();


    public void setOwner(User owner) {

        if (Objects.nonNull(this.owner) && this.owner.getRetros().contains(this)) {
            this.owner.removeRetro(this);
        }

        this.owner = owner;
        if (Objects.nonNull(owner) && !owner.getRetros().contains(this)) {
            owner.addRetro(this);
        }

    }


    public void addRetroGuest(Guest guest) {
        GuestRetro guestRetro = GuestRetro.builder()
                .guest(guest)
                .retro(this)
                .accessStatus(true)
                .accessDate(Instant.now())
                .build();
        addRetroGuest(guestRetro);
    }

    public void addRetroGuest(GuestRetro guestRetro) {
        guestRetros.add(guestRetro);

        if (Objects.nonNull(guestRetro) && !Objects.equals(guestRetro.getRetro(), this)) {
            guestRetro.setRoom(this);
        }
    }

    public void removeRetroGuest(GuestRetro guestRetro) {
        guestRetros.remove(guestRetro);

        if (Objects.nonNull(guestRetro) && Objects.equals(guestRetro.getRetro(), this)) {
            guestRetro.setRoom(null);
        }
    }


    public void addRetroMessage(RetroMessage retroMessage) {
        retroMessages.add(retroMessage);

        if (Objects.nonNull(retroMessage) && !Objects.equals(retroMessage.getRetro(), this)) {
            retroMessage.setRetro(this);
        }
    }

    public void removeRetroMessage(RetroMessage retroMessage) {
        retroMessages.remove(retroMessage);

        if (Objects.nonNull(retroMessage) && Objects.equals(retroMessage.getRetro(), this)) {
            retroMessage.setRetro(null);
        }
    }

}
