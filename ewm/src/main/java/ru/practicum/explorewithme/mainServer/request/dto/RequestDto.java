package ru.practicum.explorewithme.mainServer.request.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;

    @NotNull
    private Long requester;

    @NotNull
    private Long event;

    private String status;

    private LocalDateTime created;
}
