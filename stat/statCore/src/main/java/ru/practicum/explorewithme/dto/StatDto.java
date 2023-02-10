package ru.practicum.explorewithme.dto;

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
