package ru.practicum.explorewithme.main.server.exception.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ErrorDto {

    String message;

    String reason;

    HttpStatus status;

    List<String> errors;

    LocalDateTime timestamp;
}
