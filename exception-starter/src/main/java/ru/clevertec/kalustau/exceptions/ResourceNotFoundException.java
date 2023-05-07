package ru.clevertec.kalustau.exceptions;

/**
 * ResourceNotFoundException class extends RuntimeException class.
 * Class is thrown in case of not found entity
 * @author Dzmitry Kalustau
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
