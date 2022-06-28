package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.Vote;
import com.simplescrumpoker.model.VoteCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByRoomIdAndGuestId(Long roomId, Long guestId);

}
