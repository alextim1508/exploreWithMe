package ru.practicum.explorewithme.main.server.event.dto;

import lombok.*;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
