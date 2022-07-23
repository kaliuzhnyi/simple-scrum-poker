package com.simplescrumpoker.exception;

public class GuestNotPresentInRetroException extends RetroOperationForbidden {

    public static String MESSAGE_PATTERN = "Guest(id:%s) not present in retro(id:%s)";

    public GuestNotPresentInRetroException() {
        super();
    }

    public GuestNotPresentInRetroException(String message) {
        super(message);
    }

    public GuestNotPresentInRetroException(Long guestId, Long retroId) {
        super(MESSAGE_PATTERN.formatted(guestId, retroId));
    }

}
