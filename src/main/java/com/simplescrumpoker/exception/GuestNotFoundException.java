package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class GuestNotFoundException extends EntityNotFoundException {
    public GuestNotFoundException() {
        super();
    }

    public GuestNotFoundException(String message) {
        super(message);
    }
}
