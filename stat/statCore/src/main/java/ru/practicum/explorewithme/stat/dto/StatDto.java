package ru.practicum.explorewithme.stat.dto;

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
