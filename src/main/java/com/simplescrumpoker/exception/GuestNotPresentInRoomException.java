package com.simplescrumpoker.exception;

public class GuestNotPresentInRoomException extends OperationForbiddenException {

    public static String MESSAGE_PATTERN = "Guest(id:%s) not present in room(id:%s)";

    public GuestNotPresentInRoomException() {
        super();
    }

    public GuestNotPresentInRoomException(String message) {
        super(message);
    }

    public GuestNotPresentInRoomException(Long guestId, Long roomId) {
        super(MESSAGE_PATTERN.formatted(guestId, roomId));
    }

}
