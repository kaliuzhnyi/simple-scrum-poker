package com.simplescrumpoker.http.controller.room;

import com.simplescrumpoker.dto.guest.GuestCreateDto;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.room.RoomEnterDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.model.guest.GuestType;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/rooms/enter")
@SessionAttributes({"guest"})
@RequiredArgsConstructor
public class RoomEnterController {
    private static final String VIEW_ROOM_ENTER = "rooms/enter";
    private final RoomService roomService;
    private final GuestService guestService;

    @GetMapping
    public String enterShowPage(Model model,
                                @ModelAttribute("roomEnter") RoomEnterDto roomEnterDto,
                                @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        Optional.ofNullable(userSecurityDetailsDto)
                .map(UserSecurityDetailsDto::getName)
                .ifPresent(roomEnterDto::setGuestName);
        return VIEW_ROOM_ENTER;
    }

    @GetMapping("/{roomId}")
    public String enterShowPage(Model model,
                                @PathVariable(value = "roomId") Long roomId,
                                @ModelAttribute("roomEnter") RoomEnterDto roomEnterDto,
                                @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                                @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var guestPresentInRoom = Optional.ofNullable(guestReadDto)
                .map(v -> guestService.presentInRoom(v, roomId))
                .orElse(false);
        if (guestPresentInRoom) {
            return "redirect:/rooms/%d".formatted(roomId);
        }

        var userPresentInRoom = Optional.ofNullable(userSecurityDetailsDto)
                .map(v -> guestService.read(v.getId())
                        .orElseThrow(() -> {
                            throw new GuestNotFoundException();
                        })
                )
                .map(v -> {
                    model.addAttribute("guest", v);
                    return guestService.presentInRoom(v, roomId);
                })
                .orElse(false);
        if (userPresentInRoom) {
            return "redirect:/rooms/%d".formatted(roomId);
        }

        Optional.ofNullable(roomId)
                .ifPresent(roomEnterDto::setId);
        Optional.ofNullable(userSecurityDetailsDto)
                .map(UserSecurityDetailsDto::getName)
                .ifPresent(roomEnterDto::setGuestName);

        return VIEW_ROOM_ENTER;
    }

    @PostMapping({"", "/{roomId}"})
    public String enterRoom(Model model,
                            @PathVariable(value = "roomId", required = false) Long enterRoomId,
                            @ModelAttribute("roomEnter") @Validated RoomEnterDto roomEnterDto,
                            BindingResult bindingResult,
                            @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        if (bindingResult.hasErrors()) {
            return VIEW_ROOM_ENTER;
        }

        var roomId = Optional.ofNullable(enterRoomId).orElseGet(roomEnterDto::getId);

        var guestPresentInRoom = Optional.ofNullable(guestReadDto)
                .map(v -> {
                    if (!guestService.presentInRoom(v, roomId)) {
                        guestService.addToRoom(v, roomId);
                    }
                    return true;
                })
                .orElse(false);
        if (guestPresentInRoom) {
            return "redirect:/rooms/%d".formatted(roomId);
        }

        var userPresentInRoom = Optional.ofNullable(userSecurityDetailsDto)
                .map(v -> guestService.read(v.getId())
                        .orElseThrow(() -> {
                            throw new GuestNotFoundException();
                        })
                )
                .map(v -> {
                    if (!guestService.presentInRoom(v, roomId)) {
                        guestService.addToRoom(v, roomId);
                    }
                    return true;
                })
                .orElse(false);
        if (userPresentInRoom) {
            return "redirect:/rooms/%d".formatted(roomId);
        }

        // Anonymous guest
        var guestCreateDto = GuestCreateDto.builder()
                .name(roomEnterDto.getGuestName())
                .type(Objects.nonNull(userSecurityDetailsDto) ? GuestType.USER : GuestType.ANONYMOUS)
                .userId(Objects.nonNull(userSecurityDetailsDto) ? userSecurityDetailsDto.getId() : null)
                .roomId(roomId)
                .build();

        var newGuest = guestService.create(guestCreateDto);
        model.addAttribute("guest", newGuest);

        return "redirect:/rooms/%d".formatted(roomEnterDto.getId());
    }

    @ExceptionHandler({
            GuestNotFoundException.class
    })
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
