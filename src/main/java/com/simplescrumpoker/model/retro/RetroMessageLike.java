package com.simplescrumpoker.model.retro;

import com.simplescrumpoker.mapper.MappableEntity;
import com.simplescrumpoker.model.AuditableEntity;
import com.simplescrumpoker.model.guest.Guest;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "retro_message_likes")
public class RetroMessageLike extends AuditableEntity implements MappableEntity {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    RetroMessage message;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    Guest guest;

    public void setMessage(RetroMessage message) {

        if (Objects.nonNull(this.message) && this.message.getLikes().contains(this)) {
            this.message.removeLike(this);
        }

        this.message = message;
        if (Objects.nonNull(message) && !message.getLikes().contains(this)) {
            message.addLike(this);
        }

    }

    public void setGuest(Guest guest) {

        if (Objects.nonNull(this.guest) && this.guest.getRetroMessageLikes().contains(this)) {
            this.guest.removeRetroMessageLike(this);
        }

        this.guest = guest;
        if (Objects.nonNull(guest) && !guest.getRetroMessageLikes().contains(this)) {
            guest.addRetroMessageLike(this);
        }

    }

}
