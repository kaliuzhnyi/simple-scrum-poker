package com.simplescrumpoker.http.controller.user;

import com.simplescrumpoker.http.alert.Alert;
import com.simplescrumpoker.dto.user.UserRemindPasswordDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.user.UserSetPasswordDto;
import com.simplescrumpoker.dto.user.UserUpdatePasswordDto;
import com.simplescrumpoker.service.UserPasswordService;
import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserPasswordController {
    public static final String VIEW_PASSWORD_UPDATE = "users/password/update";
    public static final String VIEW_PASSWORD_REMIND = "users/password/remind";
    public static final String VIEW_PASSWORD_SET = "users/password/set";
    private final UserService userService;
    private final UserPasswordService userPasswordService;

    @GetMapping("/{userId}/password")
    public String updatePasswordShowPage(Model model,
                                         @PathVariable("userId") Long userId,
                                         @ModelAttribute("userUpdatePassword") UserUpdatePasswordDto userUpdatePasswordDto,
                                         @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (!userService.currentUserIdEquals(userId)) {
            return "redirect:/users/%s/password".formatted(userSecurityDetailsDto.getId());
        }
        return VIEW_PASSWORD_UPDATE;
    }

    @PatchMapping("/{userId}/password")
    public String updatePassword(Model model,
                                 @PathVariable("userId") Long userId,
                                 @ModelAttribute("userUpdatePassword") @Validated UserUpdatePasswordDto userUpdatePasswordDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (!userService.currentUserIdEquals(userId)) {
            return "redirect:/users/%s/password".formatted(userSecurityDetailsDto.getId());
        }
        if (bindingResult.hasErrors()) {
            return VIEW_PASSWORD_UPDATE;
        }
        userPasswordService.setPassword(userSecurityDetailsDto.getId(), userUpdatePasswordDto);
        model.addAttribute("alert", Alert.ofSuccess("user.set.password.success.update"));
        return VIEW_PASSWORD_UPDATE;
    }

    @GetMapping("/password/remind")
    public String remindPasswordShowPage(Model model,
                                         @ModelAttribute("userRemindPassword") UserRemindPasswordDto userRemindPasswordDto) {
        return VIEW_PASSWORD_REMIND;
    }

    @PostMapping("/password/remind")
    public String remindPassword(Model model,
                                 @ModelAttribute("userRemindPassword") @Validated UserRemindPasswordDto userRemindPasswordDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEW_PASSWORD_REMIND;
        }
        userPasswordService.remindPassword(userRemindPasswordDto.getEmail());
        model.addAttribute("alert", Alert.ofSuccess("user.remind.password.success"));
        return VIEW_PASSWORD_REMIND;
    }

    @GetMapping("/password/set/{uuid}")
    public String setPasswordShowPage(Model model,
                                      @PathVariable("uuid") String uuid,
                                      @ModelAttribute("userSetPassword") UserSetPasswordDto userSetPasswordDto,
                                      BindingResult bindingResult) {
        if (userPasswordService.remindPasswordUuidNotValid(uuid)) {
            return "redirect:/";
        }
        model.addAttribute("uuid", uuid);
        return VIEW_PASSWORD_SET;
    }

    @PostMapping("/password/set/{uuid}")
    public String setPassword(Model model,
                              RedirectAttributes redirectAttributes,
                              @PathVariable("uuid") String uuid,
                              @ModelAttribute("userSetPassword") @Validated UserSetPasswordDto userSetPasswordDto,
                              BindingResult bindingResult) {
        if (userPasswordService.remindPasswordUuidNotValid(uuid)) {
            bindingResult.addError(new ObjectError("uuid", "UUID is not correct."));
        }

        if (bindingResult.hasErrors()) {
            return VIEW_PASSWORD_SET;
        }

        userPasswordService.setPassword(uuid, userSetPasswordDto);
        redirectAttributes.addFlashAttribute("alert", Alert.ofSuccess("user.set.password.success.set"));
        return "redirect:/signin";
    }

}
