package com.simplescrumpoker.exception;

public class RetroOperationForbidden extends OperationForbiddenException {
    public RetroOperationForbidden() {
        super();
    }

    public RetroOperationForbidden(String message) {
        super(message);
    }
}
