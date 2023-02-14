package ru.practicum.explorewithme.core.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatDto {

    private String app;

    private String uri;

    private Long hits;
}
