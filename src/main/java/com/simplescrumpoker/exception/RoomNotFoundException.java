package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class RoomNotFoundException extends EntityNotFoundException {
    public static String MESSAGE_PATTERN = "Room not found by id:%s";

    public RoomNotFoundException() {
        super();
    }

    public RoomNotFoundException(String message) {
        super(message);
    }

    public RoomNotFoundException(Long roomId) {
        super(MESSAGE_PATTERN.formatted(roomId));
    }

}
