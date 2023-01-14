package ru.practicum.explorewithme.statsServer.stats.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hits {

    private Long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime created;
}
