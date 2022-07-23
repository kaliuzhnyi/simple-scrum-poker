package com.simplescrumpoker.http.controller.user;

import com.simplescrumpoker.dto.user.UserCreateDto;
import com.simplescrumpoker.service.UserService;
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
@RequestMapping("/signup")
public class SignUpController {
    public static final String VIEW_SIGNUP = "users/signup";
    private final UserService userService;

    @GetMapping
    public String signUpShowPage(Model model,
                                 @ModelAttribute("user") UserCreateDto userCreateDto) {
        return VIEW_SIGNUP;
    }

    @PostMapping
    public String signUp(Model model,
                         @ModelAttribute("user") @Validated UserCreateDto userCreateDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEW_SIGNUP;
        }
        userService.create(userCreateDto);
        return "redirect:/signin";
    }
}
