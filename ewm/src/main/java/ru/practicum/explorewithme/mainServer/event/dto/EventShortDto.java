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
public class EventShortDto {

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
    private Boolean paid;

    private Long confirmedRequests;

    private Long views;
}
