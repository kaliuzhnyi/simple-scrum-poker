package com.simplescrumpoker.http.controller;

import com.simplescrumpoker.http.alert.Alert;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.guest.RoomGuestProjection;
import com.simplescrumpoker.dto.room.RoomCreateDto;
import com.simplescrumpoker.dto.room.RoomEnterDto;
import com.simplescrumpoker.dto.room.RoomUpdateDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.vote.VoteCreateDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.exception.RoomNotFoundException;
import com.simplescrumpoker.model.VoteCard;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RoomService;
import com.simplescrumpoker.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private static final String VIEW_ROOMS = "rooms/rooms";
    private static final String VIEW_ROOM = "rooms/room";
    private static final String VIEW_ROOM_CREATE = "rooms/create";
    private static final String VIEW_ROOM_EDIT = "rooms/edit";
    private final RoomService roomService;
    private final VoteService voteService;
    private final GuestService guestService;

    @GetMapping
    public String roomsShowPage(Model model,
                                @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var ownRooms = roomService.readAll(userSecurityDetailsDto.getId());
        var enteredRooms = roomService.readAllEntered(userSecurityDetailsDto.getId());
        model.addAttribute("ownRooms", ownRooms);
        model.addAttribute("enteredRooms", enteredRooms);
        return VIEW_ROOMS;
    }

    @GetMapping(path = "/{roomId}")
    public String roomShowPage(Model model,
                               @PathVariable("roomId") Long roomId,
                               @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                               @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto,
                               HttpSession httpSession) {
        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException();
                });

        var isOwner = Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .isPresent();
        if (!isOwner) {
            var presentInRoom = Optional.ofNullable(guestReadDto)
                    .map(v -> guestService.presentInRoom(guestReadDto, roomId))
                    .orElseGet(() -> Optional.ofNullable(userSecurityDetailsDto)
                            .flatMap(guestService::findByUser)
                            .map(dto -> {
                                model.addAttribute("guest", dto);
                                return guestService.presentInRoom(dto, roomId);
                            })
                            .orElse(false));
            if (!presentInRoom) {
                return "redirect:/rooms/enter/%s".formatted(roomId);
            }
        }

        var roomGuests = roomService.readGuests(roomId);
        model.addAttribute("room", roomReadDto);
        model.addAttribute("roomGuests", roomGuests);
        model.addAttribute("roomGuestsComparatorVote", RoomGuestProjection.comparatorVote());
        model.addAttribute("roomGuestsComparatorVotePeriod", RoomGuestProjection.comparatorVotePeriod());
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("voteCards", List.of(VoteCard.values()));

        if (!model.containsAttribute("vote")) {
            model.addAttribute("vote", VoteCreateDto.builder().value(VoteCard.VALUE_UNKNOWN).build());
        }

        return VIEW_ROOM;
    }


    @GetMapping("/create")
    public String createShowPage(Model model,
                                 @ModelAttribute("room") RoomCreateDto roomCreateDto) {
        return VIEW_ROOM_CREATE;
    }

    @PostMapping("/create")
    public String create(Model model,
                         @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto,
                         @ModelAttribute("room") @Validated RoomCreateDto roomCreateDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("alert", Alert.ofError("room.create.error"));
            return VIEW_ROOM_CREATE;
        }
        return Optional.ofNullable(roomService.create(roomCreateDto, userSecurityDetailsDto.getId()))
                .map(objectDto -> {
                    //model.addAttribute("room", Alert.ofError("room.create.success"));
                    return "redirect:/rooms/%s".formatted(objectDto.getId());
                })
                .orElseGet(() -> {
                    model.addAttribute("alert", Alert.ofError("room.create.error"));
                    return VIEW_ROOM_CREATE;
                });
    }

    @GetMapping("/{roomId}/edit")
    public String editShowPage(Model model,
                               @PathVariable("roomId") Long roomId,
                               @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        return roomService.read(roomId)
                .filter(objectDto -> objectDto.getOwner().getId().equals(userSecurityDetailsDto.getId()))
                .map(objectDto -> {
                    model.addAttribute("room", objectDto);
                    return VIEW_ROOM_EDIT;
                })
                .orElse("redirect:/rooms");
    }

    @PostMapping("/{roomId}/edit")
    public String edit(Model model,
                       @PathVariable("roomId") Long roomId,
                       @ModelAttribute("room") @Validated RoomUpdateDto roomUpdateDto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("alert", Alert.ofError("room.edit.error"));
            return VIEW_ROOM_EDIT;
        }
        return roomService.update(roomId, roomUpdateDto)
                .map(objectDto -> {
                    model.addAttribute("room", objectDto);
                    model.addAttribute("alert", Alert.ofError("room.edit.success"));
                    return VIEW_ROOM_EDIT;
                })
                .orElseGet(() -> {
                    model.addAttribute("alert", Alert.ofError("room.edit.success"));
                    return VIEW_ROOM_EDIT;
                });
    }

    @PostMapping("/{roomId}/vote")
    public String vote(Model model,
                       RedirectAttributes redirectAttributes,
                       @PathVariable("roomId") Long roomId,
                       @ModelAttribute("vote") @Validated VoteCreateDto voteCreateDto,
                       BindingResult bindingResult,
                       @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                       @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException("Room not found by id:%s".formatted(roomId));
                });

        var isOwner = Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .isPresent();
        if (!isOwner) {
            var accessAllowed = Optional.ofNullable(guestReadDto)
                    .map(v -> guestService.presentInRoom(guestReadDto, roomId))
                    .orElse(false);
            if (!accessAllowed) {
                return "redirect:/rooms/enter/%s".formatted(roomId);
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("vote", voteCreateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX.concat("vote"), bindingResult);
            return "redirect:/rooms/%s".formatted(roomId);
        }

        try {
            voteService.create(voteCreateDto, roomId, guestReadDto.getId());
            redirectAttributes.addFlashAttribute("vote",
                    VoteReadDto.builder()
                            .value(voteCreateDto.getValue())
                            .build());
            return "redirect:/rooms/%s".formatted(roomId);
        } catch (Exception e) {
            model.addAttribute("alert", Alert.ofError("room.vote.add.error"));
            return VIEW_ROOM;
        }
    }

    @DeleteMapping("/{roomId}/votes")
    public String votesRemove(Model model,
                              @PathVariable("roomId") Long roomId,
                              @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException("Room not found by id:%s".formatted(roomId));
                });

        var isOwner = Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .isPresent();
        if (!isOwner) {
            return "redirect:/rooms/%s".formatted(roomId);
        }

        voteService.removeAll(roomId);

        return "redirect:/rooms/%s".formatted(roomId);
    }

    @DeleteMapping("/{roomId}/guests")
    public String guestsRemove(Model model,
                               @PathVariable("roomId") Long roomId,
                               @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var roomReadDto = roomService.read(roomId)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException("Room not found by id:%s".formatted(roomId));
                });

        var isOwner = Optional.ofNullable(userSecurityDetailsDto)
                .filter(v -> Objects.equals(v.getId(), roomReadDto.getOwner().getId()))
                .isPresent();
        if (!isOwner) {
            return "redirect:/rooms/%s".formatted(roomId);
        }

        roomService.removeGuests(roomId);

        return "redirect:/rooms/%s".formatted(roomId);
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public String handleException(RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        return Optional.ofNullable(userSecurityDetailsDto)
                .map(v -> "redirect:/rooms")
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("roomEnter", RoomEnterDto.builder().build());
                    return "redirect:/rooms/enter";
                });
    }

}
