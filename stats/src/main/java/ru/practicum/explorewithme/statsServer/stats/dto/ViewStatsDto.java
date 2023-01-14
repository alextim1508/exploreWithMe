package ru.practicum.explorewithme.statsServer.stats.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStatsDto {

    private String app;

    private String uri;

    private Long hits;
}
