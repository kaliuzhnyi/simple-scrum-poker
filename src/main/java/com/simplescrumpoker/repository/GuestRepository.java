package com.simplescrumpoker.repository;

import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.model.Guest;
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

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into " +
                    "guests_rooms (guest_id, room_id, access_date, access_status) " +
                    "values (:guestId, :roomId, now(), true)")
    void addToRoom(Long guestId, Long roomId);

    @Query(nativeQuery = true,
            value = "update " +
                    "guests_rooms " +
                    "set access_date = now(), access_status = false " +
                    "where guest_id = :guestId and room_id = :roomId")
    void blockInRoom(Long guestId, Long roomId);

    Optional<Guest> findByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "select gu.guest_id " +
                    "from guests_users gu " +
                    "where gu.user_id = :userId")
    Optional<Long> findIdByUserId(Long userId);


//    @Query(value = "select new com.simplescrumpoker.dto.guest.GuestVoteDto(" +
//            "guest.id, guest.name, guest.type, " +
//            "user.id, user.name, user.email, " +
//            "vote.id, vote.value, vote.comment, vote.lastModifiedDate, " +
//            "room.id, (owner is not null)" +
//            ") " +
//            "from Guest guest " +
//            "left join guest.user user " +
//            "inner join guest.guestRooms guestRoom " +
//            "inner join guestRoom.room room " +
//            "left join room.owner owner on user is not null and user = owner " +
//            "left join guest.votes vote on room = vote.room " +
//            "where room.id = :roomId")
//    List<GuestVoteDto> findAllGuestVotesByRoomId(Long roomId);
//, (o is not null) as isRoomOwner
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

}
