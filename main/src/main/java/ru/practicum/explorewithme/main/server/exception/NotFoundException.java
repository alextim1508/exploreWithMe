package ru.practicum.explorewithme.main.server.exception;

public class NotFoundException extends AbstractWithRejectedFieldException {

    public NotFoundException(String message, String rejectedValue) {
        super(message, rejectedValue);
    }
}
