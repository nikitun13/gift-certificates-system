package com.epam.esm.controller.handler;

import com.epam.esm.dto.ExceptionDto;
import com.epam.esm.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.joining;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("EntityNotFoundException occurred", ex);
        String message = ex.getMessage();
        int customStatusCode = CustomStatus.ENTITY_NOT_FOUND.getValue();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException occurred", ex);
        HttpStatus status = HttpStatus.CONFLICT;
        int customStatusCode = CustomStatus.DATA_INTEGRITY_VIOLATION.getValue();
        String message = "request violates some constraints";
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    protected ResponseEntity<Object> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        log.error("InvalidDataAccessApiUsageException occurred", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        int customStatusCode;
        String exceptionMessage = ex.getMessage();
        String message;
        if (exceptionMessage != null && exceptionMessage.contains("Unable to locate Attribute")) {
            int index = exceptionMessage.indexOf("]") + 1;
            message = exceptionMessage.substring(0, index);
            customStatusCode = CustomStatus.UNKNOWN_ATTRIBUTE_NAME.getValue();
        } else {
            message = "Some input params are invalid";
            customStatusCode = CustomStatus.INVALID_INPUT_PARAMS.getValue();
        }
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    protected ResponseEntity<Object> handleCannotCreateTransactionException(CannotCreateTransactionException ex) {
        log.error("CannotCreateTransactionException occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.CANNOT_ACCESS_TO_DATABASE.getValue();
        String message = "Cannot access to database";
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        log.error("DataAccessException occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.UNKNOWN_DATA_ACCESS_EXCEPTION.getValue();
        String message = "Unknown data access exception occurred";
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(Exception ex) {
        log.error("Exception occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.UNKNOWN_EXCEPTION.getValue();
        String message = "Unknown exception occurred";
        return buildResponse(status, customStatusCode, message);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            @NonNull HttpMediaTypeNotSupportedException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("HttpMediaTypeNotSupportedException occurred", ex);
        int customStatusCode = CustomStatus.MEDIA_TYPE_NOT_SUPPORTED.getValue();
        String message = ex.getMessage();
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        ResponseEntity<Object> resp = super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        return handleExceptionInternal(ex, body, resp.getHeaders(), status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("MethodArgumentNotValidException occurred", ex);
        int customStatusCode = CustomStatus.METHOD_ARGUMENT_NOT_VALID.getValue();
        String message = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining("; ", "Errors: ", "."));
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("HttpRequestMethodNotSupportedException occurred", ex);
        int customStatusCode = CustomStatus.METHOD_NOT_SUPPORTED.getValue();
        String message = ex.getMessage();
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        ResponseEntity<Object> resp = super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        return handleExceptionInternal(ex, body, resp.getHeaders(), status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("NoHandlerFoundException occurred", ex);
        int customStatusCode = CustomStatus.NO_HANDLER_FOUND.getValue();
        String message = ex.getMessage();
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        log.error("TypeMismatchException occurred", ex);
        int customStatusCode = CustomStatus.TYPE_MISMATCH.getValue();
        String message = "Type mismatch for input: " + ex.getValue();
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, int customStatusCode, String message) {
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return ResponseEntity.status(status)
                .body(body);
    }
}
