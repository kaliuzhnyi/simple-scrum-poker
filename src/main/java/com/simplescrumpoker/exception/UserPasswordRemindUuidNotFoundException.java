package com.simplescrumpoker.exception;

import javax.persistence.EntityNotFoundException;

public class UserPasswordRemindUuidNotFoundException extends EntityNotFoundException {
    public UserPasswordRemindUuidNotFoundException() {
        super();
    }
    public UserPasswordRemindUuidNotFoundException(String message) {
        super(message);
    }
}
