package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class RetroNotFoundException extends EntityNotFoundException {

    public static String MESSAGE_PATTERN = "Retro not found by id:%s";

    public RetroNotFoundException() {
        super();
    }

    public RetroNotFoundException(String message) {
        super(message);
    }

    public RetroNotFoundException(Long retroId) {
        super(MESSAGE_PATTERN.formatted(retroId));
    }

}
