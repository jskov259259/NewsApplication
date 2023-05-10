package ru.clevertec.kalustau.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.clevertec.kalustau.exceptions.AuthException;
import ru.clevertec.kalustau.exceptions.ResourceNotFoundException;

/**
 * Advice to handle exceptions thrown by the application controllers and provides a unified error responses.
 * @author Dzmitry Kalustau
 */
@ControllerAdvice
public class ExceptionHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns the response with the clarifying message and HTTP status code.
     * @param ex the exception to be handled
     * @param request the HTTP request
     * @return the resource not found exception response
     */
    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles AuthException and returns the response with the clarifying message and HTTP status code.
     * @param ex the exception to be handled
     * @param request the HTTP request
     * @return the auth exception response
     */
    @ExceptionHandler(value = { AuthException.class })
    protected ResponseEntity<Object> handleAuthException(AuthException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handles all other exceptions and returns the response with the clarifying message and HTTP status code.
     * @param ex the exception to be handled
     * @param request the HTTP request
     * @return exception response
     */
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConstraintException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
