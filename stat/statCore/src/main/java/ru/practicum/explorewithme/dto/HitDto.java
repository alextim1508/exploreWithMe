package ru.practicum.explorewithme.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HitDto {

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
