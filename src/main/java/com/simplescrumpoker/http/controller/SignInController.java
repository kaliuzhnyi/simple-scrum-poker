package com.simplescrumpoker.http.controller;

import com.simplescrumpoker.dto.user.UserEnterDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/signin")
@SessionAttributes({"guest"})
@RequiredArgsConstructor
public class SignInController implements AuthenticationSuccessHandler {
    public static final String VIEW_SIGNIN = "users/signin";

    private final GuestService guestService;

    private final RedirectStrategy redirectStrategy;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(UserSecurityDetailsDto.class::cast)
                .ifPresent(u -> {
                    guestService.findByUserId(u.getId())
                            .or(() -> {
                                throw new GuestNotFoundException();
                            })
                            .ifPresent(g -> {
                                model.addAttribute("guest", g);
                            });
                });

        return "redirect:/";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(UserSecurityDetailsDto.class::cast)
                .ifPresent(u -> {
                    guestService.findByUserId(u.getId())
                            .or(() -> {
                                throw new GuestNotFoundException();
                            })
                            .ifPresent(g -> {

                                try {
                                    redirectStrategy.sendRedirect(request, response, "/");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                request.getSession().setAttribute("guest", g);
                            });
                });
    }
}
