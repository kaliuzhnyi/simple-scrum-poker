package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class RetroMessageNotFoundException extends EntityNotFoundException {
    public RetroMessageNotFoundException() {
    }

    public RetroMessageNotFoundException(String message) {
        super(message);
    }
}
