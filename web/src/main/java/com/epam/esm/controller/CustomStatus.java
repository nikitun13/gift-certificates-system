package com.epam.esm.controller;

public enum CustomStatus {

    MISSING_SERVLET_REQUEST_PARAMETER(40001),
    SERVLET_REQUEST_BINDING(40002),
    TYPE_MISMATCH(40003),
    HTTP_MESSAGE_NOT_READABLE(40004),
    METHOD_ARGUMENT_NOT_VALID(40005),
    MISSING_SERVLET_REQUEST_PART(40006),
    BIND_EXCEPTION(40007),
    PARSING_REQUEST_EXCEPTION(40008),
    INVALID_INPUT_PARAMS(40009),

    NO_HANDLER_FOUND(40401),
    ENTITY_NOT_FOUND(40402),

    METHOD_NOT_SUPPORTED(40501),
    MEDIA_TYPE_NOT_ACCEPTABLE(40601),
    DUPLICATE_KEY_EXCEPTION(409001),
    MEDIA_TYPE_NOT_SUPPORTED(41501),

    MISSING_PATH_VARIABLE(50001),
    CONVERSION_NOT_SUPPORTED(50002),
    HTTP_MESSAGE_NOT_WRITABLE(50003),
    UNKNOWN_EXCEPTION(50004),
    UNKNOWN_DATA_ACCESS_EXCEPTION(50005),
    CANNOT_ACCESS_TO_DATABASE(50006),

    ASYNC_REQUEST_TIMEOUT(50301);

    private final int value;

    CustomStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CustomStatus{" +
                "value=" + value +
                '}';
    }
}
