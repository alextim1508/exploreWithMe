package ru.practicum.explorewithme.mainServer.exception;

public class ConflictException extends AbstractWithRejectedFieldException {

    public ConflictException(String message, String rejectedValue) {
        super(message, rejectedValue);
    }
}
