package com.simplescrumpoker.repository;

import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.model.guest.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(gr.*) > 0 then true else false end " +
                    "from guests_rooms gr " +
                    "where gr.guest_id = :guestId and gr.room_id = :roomId and gr.access_status = true")
    boolean presentInRoom(Long guestId, Long roomId);

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(gr.*) > 0 then true else false end " +
                    "from guests_retros gr " +
                    "where gr.guest_id = :guestId and gr.retro_id = :roomId and gr.access_status = true")
    boolean presentInRetro(Long guestId, Long roomId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into " +
                    "guests_rooms (guest_id, room_id, access_date, access_status) " +
                    "values (:guestId, :roomId, now(), true)")
    void addToRoom(Long guestId, Long roomId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update guests_rooms " +
                    "set access_status = false, access_date = now() " +
                    "where guest_id = :guestId and room_id = :roomId and access_date = true")
    void blockInRoom(Long guestId, Long roomId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update guests_rooms " +
                    "set access_status = false, access_date = now() " +
                    "where room_id = :roomId " +
                    "and access_status = true " +
                    "and guest_id != " +
                    "(select gu.guest_id " +
                    "from rooms r " +
                    "join guests_users gu on r.owner_id = gu.user_id " +
                    "where r.id = :roomId)")
    void blockAllInRoomExceptOwner(Long roomId);


    @Modifying
    @Query(nativeQuery = true,
            value = "insert into " +
                    "guests_retros (guest_id, retro_id, access_date, access_status) " +
                    "values (:guestId, :retroId, now(), true)")
    void addToRetro(Long guestId, Long retroId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update guests_retros " +
                    "set access_status = false, access_date = now() " +
                    "where guest_id = :guestId and retro_id = :retroId and access_date = true")
    void blockInRetro(Long guestId, Long retroId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update guests_retros " +
                    "set access_status = false, access_date = now() " +
                    "where retro_id = :retroId " +
                    "and access_status = true " +
                    "and guest_id != " +
                    "(select gu.guest_id " +
                    "from retros r " +
                    "join guests_users gu on r.owner_id = gu.user_id " +
                    "where r.id = :retroId)")
    void blockAllInRetroExceptOwner(Long retroId);

    Optional<Guest> findByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "select gu.guest_id " +
                    "from guests_users gu " +
                    "where gu.user_id = :userId")
    Optional<Long> findIdByUserId(Long userId);

    @Query(value = "select " +
            "eGuest as guest, eUser as user, eVote as vote, eRoom as room, " +
            "(eOwner is not null) as isRoomOwner " +
            "from Guest eGuest " +
            "left join eGuest.user eUser " +
            "inner join eGuest.guestRooms eGuestRoom " +
            "inner join eGuestRoom.room eRoom " +
            "left join eRoom.owner eOwner on eUser = eOwner " +
            "left join eGuest.votes eVote on eRoom = eVote.room " +
            "where eRoom.id = :roomId")
    List<GuestVoteView> findAllGuestVotesViewByRoomId(Long roomId);


    @Query(value = "select " +
            "eGuest " +
            "from Guest eGuest " +
            "join eGuest.guestRetros eGuestRetro " +
            "join eGuestRetro.retro eRetro " +
            "where eRetro.id = :retroId")
    List<Guest> findAllFromRetro(Long retroId);

}
