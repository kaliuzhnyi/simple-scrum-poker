package com.simplescrumpoker.controller;

import com.simplescrumpoker.controller.alert.Alert;
import com.simplescrumpoker.dto.user.UserEnterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signin")
public class SignInController {
    public static final String VIEW_SIGNIN = "users/signin";

    @GetMapping
    public String signInShowPage(@ModelAttribute("user") UserEnterDto userEnterDto) {
        return VIEW_SIGNIN;
    }

    @PostMapping
    public String signIn(Model model,
                         @ModelAttribute("user") @Validated UserEnterDto userEnterDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEW_SIGNIN;
        }
//        Alert.
        Alert alert = Alert.ofWarn("dd");
        return "redirect:/";
    }
}
