package com.simplescrumpoker.model.guest;

import com.simplescrumpoker.model.BaseEntity;
import com.simplescrumpoker.model.retro.Retro;
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
@Table(name = "guests_retros")
public class GuestRetro extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "retro_id")
    private Retro retro;

    @Builder.Default
    @Column(name = "access_date", nullable = false)
    private Instant accessDate = Instant.now();

    @Builder.Default
    @Column(name = "access_status")
    private Boolean accessStatus = false;

    public void setGuest(Guest guest) {

        if (Objects.nonNull(this.guest) && this.guest.getGuestRetros().contains(this)) {
            this.guest.removeRetroGuest(this);
        }

        this.guest = guest;
        if (Objects.nonNull(guest) && !guest.getGuestRetros().contains(this)) {
            guest.addRetroGuest(this);
        }

    }

    public void setRoom(Retro retro) {

        if (Objects.nonNull(this.retro) && this.retro.getGuestRetros().contains(this)) {
            this.retro.removeRetroGuest(this);
        }

        this.retro = retro;
        if (Objects.nonNull(retro) && !retro.getGuestRetros().contains(this)) {
            retro.addRetroGuest(this);
        }

    }

}
