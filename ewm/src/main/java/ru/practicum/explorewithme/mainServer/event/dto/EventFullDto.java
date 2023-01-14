package ru.practicum.explorewithme.mainServer.event.dto;

import lombok.*;
import ru.practicum.explorewithme.mainServer.category.dto.CategoryDto;
import ru.practicum.explorewithme.mainServer.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    @NonNull
    private Long id;

    @NonNull
    private String annotation;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private CategoryDto category;

    @NonNull
    private UserShortDto initiator;

    @NonNull
    private LocalDateTime eventDate;

    @NonNull
    private LocationDto location;

    @NonNull
    private Boolean paid;

    @NonNull
    private Integer participantLimit;

    private Boolean requestModeration;

    private String state;

    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    private Long confirmedRequests;

    private Long views;
}