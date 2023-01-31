package ru.practicum.explorewithme.mainServer.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    @NotNull
    @Size(max = 1024)
    private String title;

    private Boolean pinned = false;

    private List<Long> events;
}
