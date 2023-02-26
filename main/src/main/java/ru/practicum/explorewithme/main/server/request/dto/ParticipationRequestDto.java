package ru.practicum.explorewithme.main.server.request.dto;

import lombok.*;
import ru.practicum.explorewithme.main.server.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class ParticipationRequestDto {

    private Long id;

    @NotNull
    private Long requester;

    @NotNull
    private Long event;

    private RequestStatus status;

    private LocalDateTime created;
}
