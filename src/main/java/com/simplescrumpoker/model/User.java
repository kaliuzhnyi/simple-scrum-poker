package com.simplescrumpoker.model;

import com.simplescrumpoker.mapper.MappableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends AuditableEntity implements MappableEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Guest guest;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Retro> retros = new ArrayList<>();


    public void setGuest(Guest guest) {

        if (Objects.nonNull(this.guest) && Objects.equals(this.guest.getUser(), this)) {
            this.guest.getUser().setGuest(null);
        }

        this.guest = guest;
        if (Objects.nonNull(guest) && !Objects.equals(guest.getUser(), this)) {
            guest.setUser(this);
        }
    }


    public void addRoom(Room room) {
        rooms.add(room);

        if (Objects.nonNull(room) && !Objects.equals(room.getOwner(), this)) {
            room.setOwner(this);
        }
    }

    public void removeRoom(Room room) {
        rooms.remove(room);

        if (Objects.nonNull(room) && Objects.equals(room.getOwner(), this)) {
            room.setOwner(null);
        }
    }


    public void addRetro(Retro retro) {
        retros.add(retro);

        if (Objects.nonNull(retro) && !Objects.equals(retro.getOwner(), this)) {
            retro.setOwner(this);
        }
    }

    public void removeRetro(Retro retro) {
        retros.remove(retro);

        if (Objects.nonNull(retro) && Objects.equals(retro.getOwner(), this)) {
            retro.setOwner(null);
        }
    }

}
