package ru.practicum.explorewithme.main.server.request.dto;

import lombok.*;
import ru.practicum.explorewithme.main.server.request.model.RequestStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestStatusUpdateDto {

    @NonNull
    private List<Long> requestIds;

    @NonNull
    private RequestStatus status;
}
