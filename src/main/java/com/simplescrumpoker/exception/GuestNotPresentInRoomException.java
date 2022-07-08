package com.simplescrumpoker.exception;

public class GuestNotPresentInRoomException extends RuntimeException {
    public GuestNotPresentInRoomException() {
        super();
    }

    public GuestNotPresentInRoomException(String message) {
        super(message);
    }
}
