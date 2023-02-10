package ru.practicum.explorewithme.statServer.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hit {

    private Long id;

    @NonNull
    private String app;

    @NonNull
    private String uri;

    @NonNull
    private String ip;

    @NonNull
    private LocalDateTime created;
}
