package ru.practicum.explorewithme.main.server.exception;

public class MethodArgumentMismatchException extends RuntimeException {
    public MethodArgumentMismatchException(String message) {
        super(message);
    }
}
