package com.epam.esm.controller;

import com.epam.esm.dto.ExceptionDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.SymbolInterpretationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Map<Class<? extends Exception>, CustomStatus> exceptionToCustomStatus;

    public GlobalExceptionHandler() {
        exceptionToCustomStatus = initializeMap();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("EntityNotFoundException occurred", ex);
        String message = ex.getMessage();
        int customStatusCode = CustomStatus.ENTITY_NOT_FOUND.getValue();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(SymbolInterpretationException.class)
    protected ResponseEntity<Object> handleSymbolInterpretationException(SymbolInterpretationException ex) {
        logger.error("SymbolInterpretationException occurred", ex);
        String message = ex.getMessage();
        int customStatusCode = CustomStatus.PARSING_REQUEST_EXCEPTION.getValue();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.error("DuplicateKeyException occurred", ex);
        HttpStatus status = HttpStatus.CONFLICT;
        int customStatusCode = CustomStatus.DUPLICATE_KEY_EXCEPTION.getValue();
        String message = "Key already exists";
        String exceptionMessage = ex.getMessage();
        int indexOfMessage;
        if (StringUtils.isNotEmpty(exceptionMessage) &&
                (indexOfMessage = exceptionMessage.lastIndexOf("Key")) != -1) {
            message = exceptionMessage.substring(indexOfMessage);
        }
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    protected ResponseEntity<Object> handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException ex) {
        logger.error("CannotGetJdbcConnectionException occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.CANNOT_ACCESS_TO_DATABASE.getValue();
        String message = "Cannot access to database";
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        logger.error("DataAccessException occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.UNKNOWN_DATA_ACCESS_EXCEPTION.getValue();
        String message = "Unknown data access exception occurred";
        return buildResponse(status, customStatusCode, message);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(Exception ex) {
        logger.error("Exception occurred", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int customStatusCode = CustomStatus.UNKNOWN_EXCEPTION.getValue();
        String message = "Unknown exception occurred";
        return buildResponse(status, customStatusCode, message);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        logger.error("MethodArgumentNotValidException occurred", ex);
        int customStatusCode = CustomStatus.METHOD_ARGUMENT_NOT_VALID.getValue();
        String message = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining("; ", "Errors: ", "."));
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return ResponseEntity.status(status)
                .headers(headers)
                .body(body);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        logger.error("TypeMismatchException occurred", ex);
        int customStatusCode = CustomStatus.TYPE_MISMATCH.getValue();
        String message = "Type mismatch";
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return ResponseEntity.status(status)
                .headers(headers)
                .body(body);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, Object body, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status, @NonNull WebRequest request) {
        logger.error("Exception occurred", ex);
        Object newBody = body;
        if (ObjectUtils.allNull(body)) {
            int customStatusCode = exceptionToCustomStatus.getOrDefault(
                    ex.getClass(), CustomStatus.UNKNOWN_EXCEPTION).getValue();
            String message = ex.getMessage();
            newBody = new ExceptionDto(customStatusCode, message);
        }
        return super.handleExceptionInternal(ex, newBody, headers, status, request);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, int customStatusCode, String message) {
        ExceptionDto body = new ExceptionDto(customStatusCode, message);
        return ResponseEntity.status(status)
                .body(body);
    }

    private Map<Class<? extends Exception>, CustomStatus> initializeMap() {
        Map<Class<? extends Exception>, CustomStatus> map = new HashMap<>();

        map.put(HttpRequestMethodNotSupportedException.class, CustomStatus.METHOD_NOT_SUPPORTED);
        map.put(HttpMediaTypeNotSupportedException.class, CustomStatus.MEDIA_TYPE_NOT_SUPPORTED);
        map.put(HttpMediaTypeNotAcceptableException.class, CustomStatus.MEDIA_TYPE_NOT_ACCEPTABLE);
        map.put(MissingPathVariableException.class, CustomStatus.MISSING_PATH_VARIABLE);
        map.put(MissingServletRequestParameterException.class, CustomStatus.MISSING_SERVLET_REQUEST_PARAMETER);
        map.put(ServletRequestBindingException.class, CustomStatus.SERVLET_REQUEST_BINDING);
        map.put(ConversionNotSupportedException.class, CustomStatus.CONVERSION_NOT_SUPPORTED);
        map.put(TypeMismatchException.class, CustomStatus.TYPE_MISMATCH);
        map.put(HttpMessageNotReadableException.class, CustomStatus.HTTP_MESSAGE_NOT_READABLE);
        map.put(HttpMessageNotWritableException.class, CustomStatus.HTTP_MESSAGE_NOT_WRITABLE);
        map.put(MethodArgumentNotValidException.class, CustomStatus.METHOD_ARGUMENT_NOT_VALID);
        map.put(MissingServletRequestPartException.class, CustomStatus.MISSING_SERVLET_REQUEST_PART);
        map.put(BindException.class, CustomStatus.BIND_EXCEPTION);
        map.put(NoHandlerFoundException.class, CustomStatus.NO_HANDLER_FOUND);
        map.put(AsyncRequestTimeoutException.class, CustomStatus.ASYNC_REQUEST_TIMEOUT);

        return map;
    }
}
