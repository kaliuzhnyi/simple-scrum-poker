package com.simplescrumpoker.http.rest;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RoomService;
import com.simplescrumpoker.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;
    private final VoteService voteService;
    private final GuestService guestService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{roomId}/votes")
    public List<GuestVoteView> votesGet(@PathVariable("roomId") Long roomId,
                                        @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                                        @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found by id:%s".formatted(roomId));
                });

        var isOwner = Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .isPresent();
        if (!isOwner) {
            var presentInRoom = Optional.ofNullable(guestReadDto)
                    .map(v -> guestService.presentInRoom(guestReadDto, roomId))
                    .orElseGet(() -> Optional.ofNullable(userSecurityDetailsDto)
                            .flatMap(guestService::findByUser)
                            .map(v -> guestService.presentInRoom(v, roomId))
                            .orElse(false));
            if (!presentInRoom) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        List<GuestVoteView> guestVoteViews = guestService.readAllGuestVotesFromRoom(roomReadDto);
        return guestVoteViews;
    }

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
