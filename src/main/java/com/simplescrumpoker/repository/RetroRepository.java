package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.retro.Retro;
import com.simplescrumpoker.model.retro.RetroStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetroRepository extends JpaRepository<Retro, Long> {

    List<Retro> readAllByOwnerId(Long ownerId);

    @Query(nativeQuery = true,
            value = "select r.* " +
                    "from guests_users gu " +
                    "join guests_retros gr on gu.guest_id = gr.guest_id " +
                    "join retros r on r.id = gr.retro_id " +
                    "where gu.user_id = :userId")
    List<Retro> readAllEnteredByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(r.*) > 0 then true else false end " +
                    "from retros r " +
                    "where r.id = :retroId " +
                    "and ((r.password = '' and (:password is null or :password = '')) " +
                    "or r.password = cast(:password as varchar))")
    boolean passwordCorrect(@Param("retroId") Long retroId,
                            @Param("password") String password);

}
