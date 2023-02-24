package ru.practicum.explorewithme.main.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestStatusDto {

    @NotNull
    private List<ParticipationRequestDto> confirmedRequests;

    @NotNull
    private List<ParticipationRequestDto> rejectedRequests;
}
