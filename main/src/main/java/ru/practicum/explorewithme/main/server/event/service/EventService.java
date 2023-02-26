package ru.practicum.explorewithme.main.server.event.service;


import ru.practicum.explorewithme.main.server.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.dto.NewEventDto;
import ru.practicum.explorewithme.main.server.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.event.repository.EventFilter;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusUpdateDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.util.SortType;

import java.util.List;

public interface EventService {

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto getById(Long userId, Long eventId);

    EventFullDto getPublishedById(Long eventId);

    List<EventFullDto> getAll(Long userId, Integer from, Integer size);

    List<EventFullDto> getAll(EventFilter eventFilter);

    List<EventFullDto> getAll(EventFilter eventFilter, SortType sortType);

    ParticipationRequestStatusDto participationRequestStatusUpdate(
            Long userId,
            Long eventId,
            ParticipationRequestStatusUpdateDto eventRequestStatusDto);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto reject(Long userId, Long eventId, Long reqId);

    EventFullDto update(Long userId, Long eventId, UpdateEventDto event);

    EventFullDto update(Long eventId, AdminUpdateEventDto event);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    boolean isMoreRequestLimit(Event event);
}

