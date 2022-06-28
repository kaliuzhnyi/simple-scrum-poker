package com.simplescrumpoker.controller;

import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.user.UserUpdateProfileDto;
import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    public static final String VIEW_PROFILE = "users/profile";
    private final UserService userService;

    @GetMapping("/{userId}/profile")
    public String updateProfileShowPage(Model model,
                                        @PathVariable("userId") Long userId,
                                        @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (!userService.currentUserIdEquals(userId)) {
            return "redirect:/users/%s/profile".formatted(userSecurityDetailsDto.getId());
        }
        return userService.read(userId)
                .map(objectDto -> {
                    model.addAttribute("userUpdateProfile", objectDto);
                    return VIEW_PROFILE;
                })
                .orElseGet(() -> {
                    return "redirect:/";
                });
    }

    @PatchMapping("/{userId}/profile")
    public String updateProfile(Model model,
                                @PathVariable("userId") Long userId,
                                @ModelAttribute("userUpdateProfile") @Validated UserUpdateProfileDto userUpdateProfileDto,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserSecurityDetailsDto userSecurityDetailsDto) {
        if (!userService.currentUserIdEquals(userId)) {
            return "redirect:/users/%s/profile".formatted(userSecurityDetailsDto.getId());
        }
        if (bindingResult.hasErrors()) {
            return VIEW_PROFILE;
        }
        userService.update(userId, userUpdateProfileDto);
        return VIEW_PROFILE;
    }

}
