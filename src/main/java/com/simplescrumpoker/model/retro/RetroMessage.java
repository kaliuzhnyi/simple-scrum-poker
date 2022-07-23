package com.simplescrumpoker.model.retro;

import com.simplescrumpoker.mapper.MappableEntity;
import com.simplescrumpoker.model.AuditableEntity;
import com.simplescrumpoker.model.guest.Guest;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "retro_messages")
public class RetroMessage extends AuditableEntity implements MappableEntity {

    @Column(name = "value", nullable = false, length = 250)
    String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 25)
    RetroMessageType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "retro_id", nullable = false)
    Retro retro;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    Guest guest;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetroMessageLike> likes = new ArrayList<>();


    public void setRetro(Retro retro) {

        if (Objects.nonNull(this.retro) && this.retro.getRetroMessages().contains(this)) {
            this.retro.removeRetroMessage(this);
        }

        this.retro = retro;
        if (Objects.nonNull(retro) && !retro.getRetroMessages().contains(this)) {
            retro.addRetroMessage(this);
        }

    }

    public void setGuest(Guest guest) {

        if (Objects.nonNull(this.guest) && this.guest.getRetroMessages().contains(this)) {
            this.guest.removeRetroMessage(this);
        }

        this.guest = guest;
        if (Objects.nonNull(guest) && !guest.getRetroMessages().contains(this)) {
            guest.addRetroMessage(this);
        }

    }


    public void addLike(RetroMessageLike like) {
        likes.add(like);

        if (Objects.nonNull(like) && !Objects.equals(like.getMessage(), this)) {
            like.setMessage(this);
        }
    }

    public void removeLike(RetroMessageLike like) {
        likes.remove(like);

        if (Objects.nonNull(like) && Objects.equals(like.getMessage(), this)) {
            like.setMessage(null);
        }
    }

}