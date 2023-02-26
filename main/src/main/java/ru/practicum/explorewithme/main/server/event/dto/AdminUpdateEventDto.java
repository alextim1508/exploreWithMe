package ru.practicum.explorewithme.main.server.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventDto {

    private String annotation;

    private String title;

    private String description;

    @JsonProperty("category")
    private Long categoryId;

    private LocalDateTime eventDate;

    private LocationDto locationDto;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private AdminStateAction stateAction;
}