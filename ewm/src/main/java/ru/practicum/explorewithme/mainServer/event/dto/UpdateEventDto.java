package ru.practicum.explorewithme.mainServer.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {

    @NonNull
    private Long eventId;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @PositiveOrZero
    @JsonProperty("category")
    private Integer categoryId;

    @Future
    private LocalDateTime eventDate;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;
}
