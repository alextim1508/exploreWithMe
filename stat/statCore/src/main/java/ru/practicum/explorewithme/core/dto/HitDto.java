package ru.practicum.explorewithme.core.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
