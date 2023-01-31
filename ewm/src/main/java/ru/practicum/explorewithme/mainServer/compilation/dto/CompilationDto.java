package ru.practicum.explorewithme.mainServer.compilation.dto;

import lombok.*;
import ru.practicum.explorewithme.mainServer.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private Boolean pinned;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<EventShortDto> events;
}
