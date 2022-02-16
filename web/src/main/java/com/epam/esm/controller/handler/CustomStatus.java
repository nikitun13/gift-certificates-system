package com.epam.esm.controller.handler;

public enum CustomStatus {

    TYPE_MISMATCH(40001),
    METHOD_ARGUMENT_NOT_VALID(40002),
    INVALID_INPUT_PARAMS(40003),
    UNKNOWN_ATTRIBUTE_NAME(40004),

    NO_HANDLER_FOUND(40401),
    ENTITY_NOT_FOUND(40402),

    METHOD_NOT_SUPPORTED(40501),

    DATA_INTEGRITY_VIOLATION(409001),

    MEDIA_TYPE_NOT_SUPPORTED(41501),

    UNKNOWN_EXCEPTION(50001),
    UNKNOWN_DATA_ACCESS_EXCEPTION(50002),
    CANNOT_ACCESS_TO_DATABASE(50003);

    private final int value;

    CustomStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
