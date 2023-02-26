package ru.practicum.explorewithme.main.server.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    @NotNull
    @Size(max = 1024, message = "{compilation.title.length}")
    @NotBlank(message = "{compilation.title.not_blank}")
    private String title;

    private Boolean pinned = false;

    @NotNull(message = "{compilation.events.not_null}")
    private List<Long> events;
}
