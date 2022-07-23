package com.simplescrumpoker.http.controller.retro;

import com.simplescrumpoker.aop.markers.CheckGuestPermissionToEnterRetro;
import com.simplescrumpoker.aop.markers.CheckRetroStatusIsClose;
import com.simplescrumpoker.aop.markers.CheckUserPermissionToOwnRetro;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.retro.RetroCreateDto;
import com.simplescrumpoker.dto.retro.RetroUpdateDto;
import com.simplescrumpoker.dto.retro.message.RetroMessageCreateDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.http.alert.Alert;
import com.simplescrumpoker.http.helper.GuestWebHelper;
import com.simplescrumpoker.model.retro.RetroMessageType;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RetroMessageService;
import com.simplescrumpoker.service.RetroService;
import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/retros")
@RequiredArgsConstructor
public class RetroController {

    // Block of views
    private static final String VIEW = "retros";
    private static final String VIEW_RETROS = VIEW.concat("/retros");
    private static final String VIEW_RETRO = VIEW.concat("/retro");
    private static final String VIEW_RETRO_CREATE = VIEW.concat("/create");
    private static final String VIEW_RETRO_EDIT = VIEW.concat("/edit");

    // Block of services
    private final RetroService retroService;
    private final RetroMessageService retroMessageService;
    private final GuestService guestService;
    private final UserService userService;


    private final GuestWebHelper guestWebHelper;


    @GetMapping
    public String retrosView(Model model,
                             @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        Optional.ofNullable(userSecurityDetailsDto)
                .ifPresent(v -> {
                    var ownRetros = retroService.readAll(userSecurityDetailsDto.getId());
                    var enteredRetros = retroService.readAllEntered(userSecurityDetailsDto.getId());
                    model.addAttribute("ownRetros", ownRetros);
                    model.addAttribute("enteredRetros", enteredRetros);
                });
        return VIEW_RETROS;
    }

    @CheckRetroStatusIsClose
    @CheckGuestPermissionToEnterRetro
    @GetMapping("/{retroId}")
    public String retroView(Model model,
                            @PathVariable("retroId") Long retroId,
                            @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                            @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var retro = retroService.readOrThrow(retroId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();
        var isOwner = userService.userOwnRetro(userSecurityDetailsDto, retro);

        model.addAttribute("retro", retro);
        model.addAttribute("guest", currentGuest);
        model.addAttribute("isOwner", isOwner);

        if (!model.containsAttribute("lastRetroMessageType")) {
            model.addAttribute("lastRetroMessageType", null);
        }

        if (!model.containsAttribute("retroMessageWell")) {
            model.addAttribute("retroMessageWell", RetroMessageCreateDto.builder().type(RetroMessageType.WELL).build());
        }

        if (!model.containsAttribute("retroMessageBad")) {
            model.addAttribute("retroMessageBad", RetroMessageCreateDto.builder().type(RetroMessageType.BAD).build());
        }

        if (!model.containsAttribute("retroMessageStart")) {
            model.addAttribute("retroMessageStart", RetroMessageCreateDto.builder().type(RetroMessageType.START).build());
        }

        if (!model.containsAttribute("retroMessageStop")) {
            model.addAttribute("retroMessageStop", RetroMessageCreateDto.builder().type(RetroMessageType.STOP).build());
        }

        if (!model.containsAttribute("retroMessageAction")) {
            model.addAttribute("retroMessageAction", RetroMessageCreateDto.builder().type(RetroMessageType.ACTION).build());
        }

        model.addAttribute("retroMessagesWell", retroMessageService.readAllWell(retro.getId()));
        model.addAttribute("retroMessagesBad", retroMessageService.readAllBad(retro.getId()));
        model.addAttribute("retroMessagesStart", retroMessageService.readAllStart(retro.getId()));
        model.addAttribute("retroMessagesStop", retroMessageService.readAllStop(retro.getId()));
        model.addAttribute("retroMessagesAction", retroMessageService.readAllAction(retro.getId()));

        return VIEW_RETRO;
    }


    @GetMapping("/create")
    public String createView(Model model,
                             @ModelAttribute("retro") RetroCreateDto retroCreateDto) {
        return VIEW_RETRO_CREATE;
    }

    @PostMapping("/create")
    public String create(Model model,
                         @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto,
                         @ModelAttribute("retro") @Validated RetroCreateDto retroCreateDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("alert", Alert.ofError("retro.create.error"));
            return VIEW_RETRO_CREATE;
        }
        return Optional.ofNullable(retroService.create(retroCreateDto, userSecurityDetailsDto.getId()))
                .map(v -> "redirect:/retros/%s".formatted(v.getId()))
                .orElseGet(() -> {
                    model.addAttribute("alert", Alert.ofError("retro.create.error"));
                    return VIEW_RETRO_CREATE;
                });
    }


    @CheckUserPermissionToOwnRetro
    @GetMapping("/{retroId}/edit")
    public String editView(Model model,
                           @PathVariable("retroId") Long retroId,
                           @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var retro = retroService.readOrThrow(retroId);
        model.addAttribute("retro", retro);
        return VIEW_RETRO_EDIT;
    }

    @CheckUserPermissionToOwnRetro
    @PostMapping("/{retroId}/edit")
    public String edit(Model model,
                       @PathVariable("retroId") Long retroId,
                       @ModelAttribute("room") @Validated RetroUpdateDto retroUpdateDto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("alert", Alert.ofError("retro.edit.error"));
            return VIEW_RETRO_EDIT;
        }

        return retroService.update(retroId, retroUpdateDto)
                .map(v -> {
                    model.addAttribute("retro", v);
                    model.addAttribute("alert", Alert.ofSuccess("retro.edit.success"));
                    return VIEW_RETRO_EDIT;
                })
                .orElseGet(() -> {
                    model.addAttribute("alert", Alert.ofError("retro.edit.success"));
                    return VIEW_RETRO_EDIT;
                });
    }


    @CheckGuestPermissionToEnterRetro
    @PostMapping("/{retroId}/messages")
    public String messages(Model model,
                           @PathVariable("retroId") Long retroId,
                           @ModelAttribute("retroMessage") @Validated RetroMessageCreateDto retroMessageCreateDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                           @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        redirectAttributes.addFlashAttribute("lastRetroMessageType", retroMessageCreateDto.getType());

        if (bindingResult.hasErrors()) {
            Optional.of(retroMessageCreateDto)
                    .map(RetroMessageCreateDto::getType)
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .map(StringUtils::capitalize)
                    .map("retroMessage"::concat)
                    .ifPresent(v -> {
                        redirectAttributes.addFlashAttribute(v, retroMessageCreateDto);
                        redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX.concat(v), bindingResult);
                    });
            return "redirect:/retros/%s".formatted(retroId);
        }

        var retro = retroService.readOrThrow(retroId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();

        retroMessageService.create(retroMessageCreateDto, retro.getId(), currentGuest.getId());

        return "redirect:/retros/%s".formatted(retroId);
    }

    @CheckGuestPermissionToEnterRetro
    @DeleteMapping("/{retroId}/messages/{messageId}")
    public String messagesDelete(Model model,
                                 @PathVariable("retroId") Long retroId,
                                 @PathVariable("messageId") Long messageId,
                                 @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto) {
        var retro = retroService.readOrThrow(retroId);
        var retroMessage = retroMessageService.readOrThrow(messageId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();

        if (retroMessageService.guestOwnedMessage(currentGuest, retroMessage)) {
            retroMessageService.delete(retroMessage);
        }

        return "redirect:/retros/%s".formatted(retro.getId());
    }

    @CheckUserPermissionToOwnRetro
    @DeleteMapping("/{retroId}/messages")
    public String messagesDeleteAll(Model model,
                                    @PathVariable("retroId") Long retroId,
                                    @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var retroReadDto = retroService.readOrThrow(retroId);
        retroMessageService.deleteAllFromRetro(retroReadDto);
        return "redirect:/retros/%s".formatted(retroId);
    }


    @CheckGuestPermissionToEnterRetro
    @PostMapping("/{retroId}/messages/{messageId}/like")
    public String messagesLikes(Model model,
                                @PathVariable("retroId") Long retroId,
                                @PathVariable("messageId") Long messageId,
                                @SessionAttribute(value = "guest", required = false) GuestReadDto guestReadDto,
                                @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {

        var retro = retroService.readOrThrow(retroId);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();

        retroMessageService.like(messageId, currentGuest.getId());

        return "redirect:/retros/%s".formatted(retroId);
    }


    @CheckUserPermissionToOwnRetro
    @DeleteMapping("/{retroId}/guests")
    public String guestsDeleteAll(Model model,
                                  @PathVariable("retroId") Long retroId,
                                  @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        var retroReadDto = retroService.readOrThrow(retroId);
        guestService.blockAllInRetroExceptOwner(retroReadDto);
        return "redirect:/retros/%s".formatted(retroId);
    }

}
