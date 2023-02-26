package ru.practicum.explorewithme.main.server.event.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank(message = "{event.annotation.not_blank}")
    @Size(max = 2000, message = "{event.annotation.length}")
    private String annotation;

    @NotBlank(message = "{event.title.not_blank}")
    @Size(max = 120, message = "{event.title.length}")
    private String title;

    @NotBlank(message = "{event.description.not_blank}")
    @Size(max = 7000, message = "{event.description.length}")
    private String description;

    @NotNull(message = "{event.category.not_null}")
    @Positive(message = "{event.category.positive}")
    @JsonProperty("category")
    private Long categoryId;

    @NotNull(message = "{event.date.not_null}")
    private LocalDateTime eventDate;

    @NotNull(message = "{event.location.not_null}")
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "{event.participant_limit.positive_or_zero}")
    private Integer participantLimit;

    private Boolean requestModeration;
}
