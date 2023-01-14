package ru.practicum.explorewithme.statsServer.stats.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitsDto {

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
