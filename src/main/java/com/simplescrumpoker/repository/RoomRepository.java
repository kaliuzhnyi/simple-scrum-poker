package com.simplescrumpoker.repository;

import com.simplescrumpoker.dto.guest.RoomGuestProjection;
import com.simplescrumpoker.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> readAllByOwnerId(Long ownerId);

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(r.*) > 0 then true else false end " +
                    "from rooms r " +
                    "where r.id = :roomId " +
                    "and ((r.password is null and (:password is null or :password = '')) " +
                    "or r.password = cast(:password as varchar))")
    boolean passwordCorrect(@Param("roomId") Long roomId,
                            @Param("password") String password);

    @Query(nativeQuery = true,
            value = "select " +
                    "g.id, " +
                    "g.name, " +
                    "u.email, " +
                    "g.type, " +
                    "v.value vote, " +
                    "v.comment voteComment," +
                    "v.last_modified_date votePeriod " +
                    "from guests_rooms gr " +
                    "join guests g on g.id = gr.guest_id " +
                    "left join guests_users gu on gu.guest_id = g.id " +
                    "left join users u on u.id = gu.user_id " +
                    "left join votes v on v.guest_id = g.id and v.room_id = gr.room_id " +
                    "where gr.room_id = :roomId and access_status = true")
    List<RoomGuestProjection> guestsRoomGuestProjection(Long roomId);

    default <T> List<T> readAllGuests(Long roomId, Class<T> cls) {
        if (cls == RoomGuestProjection.class) {
            return (List<T>) guestsRoomGuestProjection(roomId);
        }
        return new ArrayList<T>();
    }

    @Modifying
    @Query(nativeQuery = true,
            value = "update guests_rooms gr " +
                    "set access_status = :status, access_date = now() " +
                    "where room_id = :roomId " +
                    "and access_status = true " +
                    "and guest_id != " +
                    "(select gu.guest_id " +
                    "from rooms r " +
                    "join guests_users gu on r.owner_id = gu.user_id " +
                    "where r.id = gr.room_id)")
    void setAllGuestsStatusExceptOwner(Long roomId, boolean status);

    @Query(nativeQuery = true,
            value = "select r.* " +
                    "from guests_users gu " +
                    "join guests_rooms gr on gu.guest_id = gr.guest_id " +
                    "join rooms r on r.id = gr.room_id " +
                    "where gu.user_id = :userId")
    List<Room> readAllEnteredByUserId(Long userId);

}