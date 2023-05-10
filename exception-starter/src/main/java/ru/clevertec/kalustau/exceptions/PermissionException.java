package ru.clevertec.kalustau.exceptions;

/**
 * PermissionException class extends RuntimeException class.
 * An exception is thrown when an attempt is made to perform an operation with no permissions.
 * @author Dzmitry Kalustau
 */
public class PermissionException extends RuntimeException {

    public PermissionException(String message) {
        super(message);
    }
}
