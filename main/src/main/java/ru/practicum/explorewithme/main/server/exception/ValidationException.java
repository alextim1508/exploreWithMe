package ru.practicum.explorewithme.main.server.exception;

public class ValidationException extends AbstractWithRejectedFieldException {

    public ValidationException(String message, String rejectedValue) {
        super(message, rejectedValue);
    }
}
