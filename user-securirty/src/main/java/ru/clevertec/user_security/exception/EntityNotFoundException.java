package ru.clevertec.user_security.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String message = "  not found";

    public EntityNotFoundException(Class<?> object, long id) {
        super(object.getSimpleName() + " id: " + id + message);
    }

    public EntityNotFoundException(Class<?> object) {
        super(object.getSimpleName() + message);
    }
}
