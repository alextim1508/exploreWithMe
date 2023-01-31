package ru.practicum.explorewithme.mainServer.request.service;


import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto create(Long userId, Long eventId);

    List<RequestDto> getAllByRequester(Long userId);

    RequestDto cancel(Long userId, Long requestId);
}
