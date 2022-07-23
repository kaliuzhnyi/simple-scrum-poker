package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.retro.RetroMessage;
import com.simplescrumpoker.model.retro.RetroMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RetroMessageRepository extends JpaRepository<RetroMessage, Long> {

    List<RetroMessage> readAllByRetroIdAndType(Long retroId, RetroMessageType type);

    @Modifying
    @Query(value = "delete " +
            "from RetroMessage eRetroMessage " +
            "where eRetroMessage.retro.id = :retroId")
    void deleteAllFromRetro(Long retroId);
}
