package ru.practicum.explorewithme.mainServer.event.service;

import ru.practicum.explorewithme.mainServer.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.event.dto.NewEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.model.Event;
import ru.practicum.explorewithme.mainServer.event.model.EventState;
import ru.practicum.explorewithme.mainServer.event.repository.EventFilter;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;
import ru.practicum.explorewithme.mainServer.util.SortType;

import java.util.List;

public interface EventService {

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto get(Long userId, Long eventId);

    EventFullDto getPublishedById(Long eventId);

    List<EventFullDto> getAll(Long userId, Integer from, Integer size);

    List<EventFullDto> getAll(EventFilter eventFilter);

    List<EventFullDto> getAll(EventFilter eventFilter, SortType sortType);

    EventFullDto cancel(Long userId, Long eventId);

    EventFullDto setEventState(Long eventId, EventState state);

    RequestDto confirm(Long userId, Long eventId, Long reqId);

    RequestDto reject(Long userId, Long eventId, Long reqId);

    EventFullDto update(Long userId, UpdateEventDto event);

    EventFullDto update(Long eventId, AdminUpdateEventDto event);

    List<RequestDto> getRequests(Long userId, Long eventId);

    boolean isMoreRequestLimit(Event event);
}

