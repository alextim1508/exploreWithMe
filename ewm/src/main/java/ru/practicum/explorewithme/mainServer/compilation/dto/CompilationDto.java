package ru.practicum.explorewithme.mainServer.compilation.dto;

import lombok.*;
import ru.practicum.explorewithme.mainServer.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private Boolean pinned;

    private List<EventShortDto> events;
}
