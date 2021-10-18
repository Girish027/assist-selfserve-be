package com.tfsc.ilabs.selfservice.common.controller;

import com.tfsc.ilabs.selfservice.common.exception.ResourceException;
import com.tfsc.ilabs.selfservice.common.exception.SelfServeException;
import com.tfsc.ilabs.selfservice.common.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.tfsc.ilabs.selfservice.common.utils.Constants.*;

/**
 * Created by ravi.b on 31-05-2019.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private static final String MESSAGE = "message";

    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<Object> handleResourceException(ResourceException ex) {
        logger.error("standard exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(SelfServeException.class)
    public ResponseEntity<Object> handleSelfServeException(SelfServeException ex) {
        logger.error("selfserve exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("No such element Exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        logger.error("Access denied Exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("Message Not Readable Exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(MESSAGE_NOT_READABLE_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.error("Message Argument Type Mismatch Exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE + " : " + ex.getName()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal Argument Exception received: {}", ex.getMessage());
        return new ResponseEntity<>(createResponse(ILLEGAL_ARGUMENT_ERROR_MESSAGE + " : "), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleUnknownException(Throwable ex) throws javax.servlet.ServletException {
        logger.error("non-standard exception received: {}", ex.getMessage());
        if (ex instanceof javax.servlet.ServletException) {
            // these exceptions are thrown by Spring boot. For example HttpMediaTypeNotSupportedException (for 405)
            throw (javax.servlet.ServletException) ex;
        }

        return new ResponseEntity<>(createResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Object createResponse(String message) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(MESSAGE, message);
        return responseBody;
    }

    private Object createResponse(Throwable ex) {
        if (ex instanceof ResourceException) {
            return ((ResourceException) ex).getPropertyErrors();
        }

        Map<String, String> responseBody = new HashMap<>();
        if (ex instanceof ResourceException) {
            responseBody.put(MESSAGE, ex.getLocalizedMessage());
        } else {
            responseBody.put(MESSAGE, Constants.DEFAULT_ERROR_MESSAGE);
        }
        return responseBody;
    }
}
