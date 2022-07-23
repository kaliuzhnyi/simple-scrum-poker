package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class RetroIsCloseException extends EntityNotFoundException {

    public static String MESSAGE_PATTERN = "Retro(id:%s) is close";

    public RetroIsCloseException() {
        super();
    }

    public RetroIsCloseException(String message) {
        super(message);
    }

    public RetroIsCloseException(Long retroId) {
        super(MESSAGE_PATTERN.formatted(retroId));
    }

}
