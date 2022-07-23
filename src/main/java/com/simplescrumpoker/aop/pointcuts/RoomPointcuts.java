package com.simplescrumpoker.aop.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoomPointcuts {

    @Pointcut("com.simplescrumpoker.aop.pointcuts.CommonPointcuts.isController() && within(com.simplescrumpoker.http.controller.room.*)")
    public void isController() {
    }

    @Pointcut("@annotation(com.simplescrumpoker.aop.markers.CheckGuestPermissionToEnterRoom)")
    public void guestHasPermissionToEnterRoom() {
    }

    @Pointcut("@annotation(com.simplescrumpoker.aop.markers.CheckUserPermissionToOwnRoom)")
    public void userHasPermissionToOwnRoom() {
    }

}