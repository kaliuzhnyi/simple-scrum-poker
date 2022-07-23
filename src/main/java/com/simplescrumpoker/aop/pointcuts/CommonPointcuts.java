package com.simplescrumpoker.aop.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isController() {
    }

}
