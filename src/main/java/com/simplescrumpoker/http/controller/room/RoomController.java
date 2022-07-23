package com.simplescrumpoker.http.controller.room;

import com.simplescrumpoker.aop.markers.CheckGuestPermissionToEnterRoom;
import com.simplescrumpoker.aop.markers.CheckUserPermissionToOwnRetro;
import com.simplescrumpoker.aop.markers.CheckUserPermissionToOwnRoom;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.dto.room.RoomCreateDto;
import com.simplescrumpoker.dto.room.RoomEnterDto;
import com.simplescrumpoker.dto.room.RoomUpdateDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.vote.VoteCreateDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.exception.GuestNotPresentInRoomException;
import com.simplescrumpoker.exception.RoomNotFoundException;
import com.simplescrumpoker.http.alert.Alert;
import com.simplescrumpoker.http.helper.GuestWebHelper;
import com.simplescrumpoker.model.VoteCard;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RoomService;
import com.simplescrumpoker.service.UserService;
import com.simplescrumpoker.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    // Block of views
    private static final String VIEW = "rooms";
    private static final String VIEW_ROOMS = VIEW.concat("/rooms");
    private static final String VIEW_ROOM = VIEW.concat("/room");
    private static final String VIEW_ROOM_CREATE = VIEW.concat("/create");
    private static final String VIEW_ROOM_EDIT = VIEW.concat("/edit");

    // Block of services
    private final RoomService roomService;
    private final VoteService voteService;
    private final GuestService guestService;
    private final UserService userService;


    private final GuestWebHelper guestWebHelper;


    @GetMapping
    public String roomsView(Model model,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var ownRooms = roomService.readAll(userSecurityDetailsDto.getId());
        var enteredRooms = roomService.readAllEntered(userSecurityDetailsDto.getId());
        model.addAttribute("ownRooms", ownRooms);
        model.addAttribute("enteredRooms", enteredRooms);
        return VIEW_ROOMS;
    }

    @CheckGuestPermissionToEnterRoom
    @GetMapping(path = "/{roomId}")
    public String roomView(Model model,
                           @PathVariable("roomId") Long roomId,
                           @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                           @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var room = roomService.readOrThrow(roomId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();
        var isOwner = userService.userOwnRoom(userSecurityDetailsDto, room);

        var guestVotes = guestService.readAllGuestVotesFromRoom(room);

        var currentGuestVote = guestVotes.stream()
                .filter(v -> v.getGuest().getId().equals(currentGuest.getId()))
                .findFirst()
                .orElse(null);

        model.addAttribute("room", room);
        model.addAttribute("currentGuestVote", currentGuestVote);
        model.addAttribute("guestVotes", guestVotes);
        model.addAttribute("guestVotesComparatorVote", GuestVoteView.comparatorVote());
        model.addAttribute("guestVotesComparatorVotePeriod", GuestVoteView.comparatorVotePeriod());
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("voteCards", List.of(VoteCard.values()));

        if (!model.containsAttribute("vote")) {
            model.addAttribute("vote", VoteCreateDto.builder().value(VoteCard.VALUE_UNKNOWN).build());
        }

        return VIEW_ROOM;
    }


    @GetMapping("/create")
    public String createView(Model model,
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
                .map(v -> {
                    return "redirect:/rooms/%s".formatted(v.getId());
                })
                .orElseGet(() -> {
                    model.addAttribute("alert", Alert.ofError("room.create.error"));
                    return VIEW_ROOM_CREATE;
                });
    }

    @CheckUserPermissionToOwnRoom
    @GetMapping("/{roomId}/edit")
    public String editView(Model model,
                           @PathVariable("roomId") Long roomId,
                           @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var room = roomService.readOrThrow(roomId);
        model.addAttribute("room", room);
        return VIEW_ROOM_EDIT;
    }

    @CheckUserPermissionToOwnRoom
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

    @CheckGuestPermissionToEnterRoom
    @PostMapping("/{roomId}/vote")
    public String vote(Model model,
                       RedirectAttributes redirectAttributes,
                       @PathVariable("roomId") Long roomId,
                       @ModelAttribute("vote") @Validated VoteCreateDto voteCreateDto,
                       BindingResult bindingResult,
                       @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                       @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("vote", voteCreateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX.concat("vote"), bindingResult);
            return "redirect:/rooms/%s".formatted(roomId);
        }

        var room = roomService.readOrThrow(roomId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();

        try {
            voteService.create(voteCreateDto, roomId, currentGuest.getId());
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

    @CheckUserPermissionToOwnRoom
    @DeleteMapping("/{roomId}/votes")
    public String votesRemove(Model model,
                              @PathVariable("roomId") Long roomId,
                              @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var room = roomService.readOrThrow(roomId);
        voteService.removeAll(roomId);
        return "redirect:/rooms/%s".formatted(roomId);
    }

    @CheckUserPermissionToOwnRoom
    @DeleteMapping("/{roomId}/guests")
    public String guestsRemove(Model model,
                               @PathVariable("roomId") Long roomId,
                               @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var room = roomService.readOrThrow(roomId);
        guestService.blockAllInRoomExceptOwner(room);
        return "redirect:/rooms/%s".formatted(roomId);
    }

}
