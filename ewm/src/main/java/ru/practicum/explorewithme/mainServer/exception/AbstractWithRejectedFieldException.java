package ru.practicum.explorewithme.mainServer.exception;

public abstract class AbstractWithRejectedFieldException extends RuntimeException {
    private String rejectedValue;

    public AbstractWithRejectedFieldException(String message, String rejectedValue) {
        super(message);
        this.rejectedValue = rejectedValue;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }
}
