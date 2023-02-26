package ru.practicum.explorewithme.main.server.request.service;


import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getAllByRequester(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
