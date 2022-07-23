package com.simplescrumpoker.aop.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetroPointcuts {

    @Pointcut("com.simplescrumpoker.aop.pointcuts.CommonPointcuts.isController() && within(com.simplescrumpoker.http.controller.retro.*)")
    public void isController() {
    }

    @Pointcut("@annotation(com.simplescrumpoker.aop.markers.CheckGuestPermissionToEnterRetro)")
    public void guestHasPermissionToEnterRetro() {
    }

    @Pointcut("@annotation(com.simplescrumpoker.aop.markers.CheckUserPermissionToOwnRetro)")
    public void userHasPermissionToOwnRetro() {
    }

    @Pointcut("@annotation(com.simplescrumpoker.aop.markers.CheckRetroStatusIsClose)")
    public void retroIsClose() {
    }

}