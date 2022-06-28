package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class RoomNotFoundException extends EntityNotFoundException {
    public RoomNotFoundException() {
        super();
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
