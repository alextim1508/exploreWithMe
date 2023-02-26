package ru.practicum.explorewithme.main.server.compilation.dto;

import lombok.*;
import ru.practicum.explorewithme.main.server.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "events"})
@ToString(exclude = "events")
public class CompilationDto {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private Boolean pinned;

    private List<EventShortDto> events;
}
