package ru.practicum.explorewithme.main.server.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDto {

    private String title;

    private Boolean pinned = false;

    private List<Long> events;

}
