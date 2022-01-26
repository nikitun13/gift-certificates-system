package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Entity not found (id = %s)";

    public EntityNotFoundException(Object id) {
        super(MESSAGE_FORMAT.formatted(id));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Object id, Throwable cause) {
        super(MESSAGE_FORMAT.formatted(id), cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
