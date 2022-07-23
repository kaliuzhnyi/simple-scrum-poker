package com.simplescrumpoker.aop;

import com.simplescrumpoker.dto.retro.RetroEnterDto;
import com.simplescrumpoker.exception.GuestNotPresentInRetroException;
import com.simplescrumpoker.exception.RetroIsCloseException;
import com.simplescrumpoker.exception.UserNotOwnRetroException;
import com.simplescrumpoker.http.helper.GuestWebHelper;
import com.simplescrumpoker.http.helper.UserWebHelper;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.RetroService;
import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class RetroAspect {

    private final GuestWebHelper guestWebHelper;
    private final UserWebHelper userWebHelper;

    private final RetroService retroService;
    private final GuestService guestService;
    private final UserService userService;

    @Before("com.simplescrumpoker.aop.pointcuts.RetroPointcuts.isController() && com.simplescrumpoker.aop.pointcuts.RetroPointcuts.retroIsClose()")
    public void checkRetroStatusIsClose(JoinPoint joinPoint) {

        var retroId = MethodProcessingHelper.getMethodParameterByName(joinPoint, "retroId", Long.class);
        if (Objects.isNull(retroId)) {
            var retroEnter = MethodProcessingHelper.getMethodParameterByName(joinPoint, "retroEnter", RetroEnterDto.class);
            if (Objects.nonNull(retroEnter)) {
                retroId = retroEnter.getId();
            } else {
                return;
            }
        }

        var currentUser = userWebHelper.defineCurrentUser();
        if (currentUser.isEmpty()) {
            if (retroService.hasStatusClose(retroId)) {
                throw new RetroIsCloseException(retroId);
            }
        } else {
            var isOwner = userService.userOwnRetro(currentUser.get().getId(), retroId);
            if (!isOwner && retroService.hasStatusClose(retroId)) {
                throw new RetroIsCloseException(retroId);
            }
        }

    }

    @Before("com.simplescrumpoker.aop.pointcuts.RetroPointcuts.isController() && com.simplescrumpoker.aop.pointcuts.RetroPointcuts.guestHasPermissionToEnterRetro()")
    public void checkGuestHasPermissionToEnterRetro(JoinPoint joinPoint) {

        var retroId = MethodProcessingHelper.getMethodParameterByName(joinPoint, "retroId", Long.class);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();
        var guestId = currentGuest.getId();
        var guestPresentInRetro = guestService.presentInRetro(guestId, retroId);
        if (!guestPresentInRetro) {
            throw new GuestNotPresentInRetroException(guestId, retroId);
        }

    }

    @Before("com.simplescrumpoker.aop.pointcuts.RetroPointcuts.isController() && com.simplescrumpoker.aop.pointcuts.RetroPointcuts.userHasPermissionToOwnRetro()")
    public void checkUserHasPermissionToOwnRetro(JoinPoint joinPoint) {

        var retroId = MethodProcessingHelper.getMethodParameterByName(joinPoint, "retroId", Long.class);
        var currentUser = userWebHelper.defineCurrentUserOrThrow();
        var userId = currentUser.getId();

        var userOwnRetro = userService.userOwnRetro(userId, retroId);
        if (!userOwnRetro) {
            throw new UserNotOwnRetroException();
        }

    }

}
