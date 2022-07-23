package com.simplescrumpoker.http.controller.retro;

import com.simplescrumpoker.aop.markers.CheckRetroStatusIsClose;
import com.simplescrumpoker.dto.guest.GuestCreateDto;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.retro.RetroEnterDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.model.guest.GuestType;
import com.simplescrumpoker.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/retros/enter")
@SessionAttributes({"guest"})
@RequiredArgsConstructor
public class RetroEnterController {

    // Block of views
    private static final String VIEW = "retros";
    private static final String VIEW_RETRO_ENTER = VIEW.concat("/enter");

    // Block of services
    private final GuestService guestService;

    @GetMapping
    public String enterView(Model model,
                            @ModelAttribute("retroEnter") RetroEnterDto retroEnterDto,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        Optional.ofNullable(userSecurityDetailsDto)
                .map(UserSecurityDetailsDto::getName)
                .ifPresent(retroEnterDto::setGuestName);
        return VIEW_RETRO_ENTER;
    }

    @CheckRetroStatusIsClose
    @GetMapping("/{retroId}")
    public String enterView(Model model,
                            @PathVariable(value = "retroId") Long retroId,
                            @ModelAttribute("retroEnter") RetroEnterDto retroEnterDto,
                            @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var guestPresentInRetro = Optional.ofNullable(guestReadDto)
                .map(v -> guestService.presentInRetro(v.getId(), retroId))
                .orElse(false);
        if (guestPresentInRetro) {
            return "redirect:/retros/%d".formatted(retroId);
        }

        var userPresentInRetro = Optional.ofNullable(userSecurityDetailsDto)
                .flatMap(guestService::findByUser)
                .map(v -> {
                    model.addAttribute("guest", v);
                    return guestService.presentInRoom(v, retroId);
                })
                .orElse(false);
        if (userPresentInRetro) {
            return "redirect:/retros/%d".formatted(retroId);
        }

        Optional.ofNullable(retroId)
                .ifPresent(retroEnterDto::setId);
        Optional.ofNullable(userSecurityDetailsDto)
                .map(UserSecurityDetailsDto::getName)
                .ifPresent(retroEnterDto::setGuestName);

        return VIEW_RETRO_ENTER;
    }

    @CheckRetroStatusIsClose
    @PostMapping({"", "/{retroId}"})
    public String enter(Model model,
                        @PathVariable(value = "retroId", required = false) Long enterRetroId,
                        @ModelAttribute("retroEnter") @Validated RetroEnterDto retroEnterDto,
                        BindingResult bindingResult,
                        @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                        @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        if (bindingResult.hasErrors()) {
            return VIEW_RETRO_ENTER;
        }

        var retroId = Optional.ofNullable(enterRetroId).orElseGet(retroEnterDto::getId);

        var guestPresentInRetro = Optional.ofNullable(guestReadDto)
                .map(v -> {
                    if (!guestService.presentInRetro(v.getId(), retroId)) {
                        guestService.addToRetro(v, retroId);
                    }
                    return true;
                })
                .orElse(false);
        if (guestPresentInRetro) {
            return "redirect:/retros/%d".formatted(retroId);
        }

        var userPresentInRetro = Optional.ofNullable(userSecurityDetailsDto)
                .flatMap(guestService::findByUser)
                .map(v -> {
                    if (!guestService.presentInRoom(v, retroId)) {
                        guestService.addToRetro(v, retroId);
                    }
                    return true;
                })
                .orElse(false);
        if (userPresentInRetro) {
            return "redirect:/retros/%d".formatted(retroId);
        }

        // Anonymous guest
        var guestCreateDto = GuestCreateDto.builder()
                .name(retroEnterDto.getGuestName())
                .type(Objects.nonNull(userSecurityDetailsDto) ? GuestType.USER : GuestType.ANONYMOUS)
                .userId(Objects.nonNull(userSecurityDetailsDto) ? userSecurityDetailsDto.getId() : null)
                .retroId(retroId)
                .build();

        var newGuest = guestService.create(guestCreateDto);
        model.addAttribute("guest", newGuest);

        return "redirect:/retros/%d".formatted(retroId);
    }

}
