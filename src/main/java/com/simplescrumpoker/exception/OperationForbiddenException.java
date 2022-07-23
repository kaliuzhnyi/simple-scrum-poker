package com.simplescrumpoker.exception;

public class OperationForbiddenException extends RuntimeException {
    public OperationForbiddenException() {
        super();
    }

    public OperationForbiddenException(String message) {
        super(message);
    }
}
