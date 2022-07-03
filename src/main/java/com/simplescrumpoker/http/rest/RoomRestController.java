package com.simplescrumpoker.http.rest;

import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.service.RoomService;
import com.simplescrumpoker.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;
    private final VoteService voteService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{roomId}/votes")
    public void votesRemove(@PathVariable("roomId") Long roomId,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found by id:%s".formatted(roomId));
                });

        Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .or(() -> {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                });

        voteService.removeAll(roomId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{roomId}/guests")
    public void guestsRemove(@PathVariable("roomId") Long roomId,
                             @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found by id:%s".formatted(roomId));
                });

        Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .or(() -> {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                });

        roomService.removeGuests(roomId);
    }

}
