package ru.practicum.explorewithme.main.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.explorewithme.main.server.exception.dto.ErrorDto;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getFieldError().getDefaultMessage())
                .reason(String.valueOf(e.getFieldError().getRejectedValue()))
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason(e.getParameterType() + " " + e.getParameterName())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason(e.getErrorCode())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleConflictException(ConflictException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason(e.getRejectedValue())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(NotFoundException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason(e.getRejectedValue())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(ValidationException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason(e.getRejectedValue())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleConstraintViolationException(ConstraintViolationException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason("")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            InvalidDataAccessApiUsageException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleHttpException(RuntimeException e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason("")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());
        return errorDto;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleUnknownException(Exception e) {

        ErrorDto errorDto = ErrorDto.builder()
                .errors(Collections.emptyList())
                .message(e.getMessage())
                .reason("")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();

        log.error(errorDto.toString());

        e.printStackTrace();

        return errorDto;
    }
}
