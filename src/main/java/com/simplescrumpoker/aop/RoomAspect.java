package com.simplescrumpoker.aop;

import com.simplescrumpoker.exception.GuestNotPresentInRetroException;
import com.simplescrumpoker.exception.GuestNotPresentInRoomException;
import com.simplescrumpoker.exception.UserNotOwnRetroException;
import com.simplescrumpoker.exception.UserNotOwnRoomException;
import com.simplescrumpoker.http.helper.GuestWebHelper;
import com.simplescrumpoker.http.helper.UserWebHelper;
import com.simplescrumpoker.service.GuestService;
import com.simplescrumpoker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RoomAspect {

    private final GuestWebHelper guestWebHelper;
    private final UserWebHelper userWebHelper;

    private final GuestService guestService;
    private final UserService userService;


    @Before("com.simplescrumpoker.aop.pointcuts.RoomPointcuts.isController() && com.simplescrumpoker.aop.pointcuts.RoomPointcuts.guestHasPermissionToEnterRoom()")
    public void checkGuestHasPermissionToEnterRoom(JoinPoint joinPoint) {

        var roomId = MethodProcessingHelper.getMethodParameterByName(joinPoint, "roomId", Long.class);
        var currentGuest = guestWebHelper.defineCurrentGuestOrThrow();
        var guestId = currentGuest.getId();
        var guestPresentInRoom = guestService.presentInRoom(guestId, roomId);
        if (!guestPresentInRoom) {
            throw new GuestNotPresentInRoomException(guestId, roomId);
        }

    }

    @Before("com.simplescrumpoker.aop.pointcuts.RoomPointcuts.isController() && com.simplescrumpoker.aop.pointcuts.RoomPointcuts.userHasPermissionToOwnRoom()")
    public void checkUserHasPermissionToOwnRoom(JoinPoint joinPoint) {

        var roomId = MethodProcessingHelper.getMethodParameterByName(joinPoint, "roomId", Long.class);
        var currentUser = userWebHelper.defineCurrentUserOrThrow();
        var userId = currentUser.getId();

        var userOwnRoom = userService.userOwnRoom(userId, roomId);
        if (!userOwnRoom) {
            throw new UserNotOwnRoomException();
        }

    }

}
