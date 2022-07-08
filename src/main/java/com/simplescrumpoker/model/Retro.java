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
@Table(name = "retros")
public class Retro extends AuditableEntity implements MappableEntity {

    @Column(name = "title", nullable = false, length = 50)
    String title;

    @Column(name = "description", length = 1000)
    String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RetroStatus status = RetroStatus.UNBLOCK;

    @Column(name = "password", nullable = false, length = 100)
    String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    public void setOwner(User owner) {

        if (Objects.nonNull(this.owner) && this.owner.getRetros().contains(this)) {
            this.owner.removeRetro(this);
        }

        this.owner = owner;
        if (Objects.nonNull(owner) && !owner.getRetros().contains(this)) {
            owner.addRetro(this);
        }

    }

}
