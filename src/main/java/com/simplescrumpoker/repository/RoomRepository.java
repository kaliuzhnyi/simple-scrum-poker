package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> readAllByOwnerId(Long ownerId);

    @Query(nativeQuery = true,
            value = "select r.* " +
                    "from guests_users gu " +
                    "join guests_rooms gr on gu.guest_id = gr.guest_id " +
                    "join rooms r on r.id = gr.room_id " +
                    "where gu.user_id = :userId")
    List<Room> readAllEnteredByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(r.*) > 0 then true else false end " +
                    "from rooms r " +
                    "where r.id = :roomId " +
                    "and ((r.password is null and (:password is null or :password = '')) " +
                    "or r.password = cast(:password as varchar))")
    boolean passwordCorrect(@Param("roomId") Long roomId,
                            @Param("password") String password);

}