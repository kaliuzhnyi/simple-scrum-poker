package com.simplescrumpoker.http.exception;

import com.simplescrumpoker.exception.*;
import com.simplescrumpoker.http.alert.Alert;
import com.simplescrumpoker.http.alert.AlertMessageType;
import com.simplescrumpoker.http.controller.PackageMarker;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackageClasses = PackageMarker.class)
class ExceptionController {

    @ExceptionHandler(RoomNotFoundException.class)
    public String handleExceptionRoomNotFound(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofWarn("Room not found. Try to enter to other room.", AlertMessageType.TEXT));
        return "redirect:/rooms/enter";
    }

    @ExceptionHandler(RetroNotFoundException.class)
    public String handleExceptionRetroNotFound(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofWarn("Retro not found. Try to enter to other retro.", AlertMessageType.TEXT));
        return "redirect:/retros/enter";
    }

    @ExceptionHandler(RetroIsCloseException.class)
    public String handleExceptionRetroIsClose(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofWarn("Retro is close. Try to connect with retro-owner.", AlertMessageType.TEXT));
        return "redirect:/retros/enter";
    }

    @ExceptionHandler({GuestNotFoundException.class, GuestNotDefineException.class})
    public String handleExceptionGuestNotFound(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofWarn("You are not authorized. Try to signin.", AlertMessageType.TEXT));
        return "redirect:/signin";
    }

    @ExceptionHandler(GuestNotPresentInRoomException.class)
    public String handleExceptionGuestNotPresentInRoom(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofError("You are not present in room. Try to enter.", AlertMessageType.TEXT));
        return "redirect:/rooms/enter";
    }

    @ExceptionHandler(GuestNotPresentInRetroException.class)
    public String handleExceptionGuestNotPresentInRetro(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofError("You are not present in retro. Try to enter.", AlertMessageType.TEXT));
        return "redirect:/retros/enter";
    }

    @ExceptionHandler(UserNotOwnRetroException.class)
    public String handleExceptionUserNotOwnedRetroException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alert", Alert.ofWarn("You are not owned the retro. Try to signin.", AlertMessageType.TEXT));
        return "redirect:/signin";
    }

}
