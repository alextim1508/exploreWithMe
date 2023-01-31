package ru.practicum.explorewithme.mainServer.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.mainServer.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.event.dto.NewEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.model.Event;
import ru.practicum.explorewithme.mainServer.event.model.EventState;
import ru.practicum.explorewithme.mainServer.event.repository.EventFilter;
import ru.practicum.explorewithme.mainServer.event.repository.EventRepository;
import ru.practicum.explorewithme.mainServer.event.repository.EventSpecification;
import ru.practicum.explorewithme.mainServer.exception.ConflictException;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.exception.ValidationException;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;
import ru.practicum.explorewithme.mainServer.request.model.Request;
import ru.practicum.explorewithme.mainServer.request.service.RequestMapper;
import ru.practicum.explorewithme.mainServer.user.model.User;
import ru.practicum.explorewithme.mainServer.user.service.UserFactory;
import ru.practicum.explorewithme.mainServer.util.OffsetLimitPageable;
import ru.practicum.explorewithme.mainServer.util.SortType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explorewithme.mainServer.request.model.RequestStatus.CONFIRMED;
import static ru.practicum.explorewithme.mainServer.request.model.RequestStatus.REJECTED;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repo;
    private final EventMapper mapper;

    private final UserFactory userFactory;

    private final RequestMapper requestMapper;

    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        Event event = mapper.toEntity(eventDto, userId);
        event.setState(EventState.PENDING);

        Event savedEvent = repo.save(event);
        log.info("Event {} is created", savedEvent);

        return mapper.toFullDto(savedEvent);
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        User user = userFactory.getById(userId);

        Event event = repo.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Event is not found",
                        format("EventId=%d, InitiatorId=%d", eventId, userId)));
        log.info("{} is found", event);

        return mapper.toFullDto(event);
    }

    @Override
    public EventFullDto getPublishedById(Long eventId) {
        Event event = repo.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        "Event is not found or is not published yet",
                        format("Id=%d", eventId)));

        log.info("{} is found", event);

        return mapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(Long userId, Integer from, Integer size) {
        List<Event> events = repo.findAllByInitiatorId(userId, OffsetLimitPageable.of(from, size));
        log.info("{} events are founded", events.size());

        return mapper.toFullDto(events);
    }

    @Override
    public List<EventFullDto> getAll(EventFilter eventFilter) {
        List<Event> events = repo.findAll(
                        new EventSpecification(eventFilter),
                        OffsetLimitPageable.of(eventFilter.getFrom(), eventFilter.getSize()))
                .getContent();
        log.info("{} events are founded", events.size());

        return mapper.toFullDto(events);
    }

    @Override
    public List<EventFullDto> getAll(EventFilter eventFilter, SortType sortType) {
        List<Event> events = repo.findAll(
                        new EventSpecification(eventFilter),
                        OffsetLimitPageable.of(eventFilter.getFrom(), eventFilter.getSize()))
                .getContent();

        if (eventFilter.getOnlyAvailable() != null && eventFilter.getOnlyAvailable()) {
            events = events.stream().filter(e -> !isMoreRequestLimit(e)).collect(Collectors.toList());
        }

        log.info("{} events are founded", events.size());

        return mapper.toFullDto(events);
    }

    @Transactional
    @Override
    public EventFullDto cancel(Long userId, Long eventId) {
        Event event = repo.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Event is not found",
                        format("EventId=%d, InitiatorId=%d", eventId, userId)));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Published event can not be cancelled", event.getState().toString());
        }
        event.setState(EventState.CANCELED);

        return mapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto setEventState(Long eventId, EventState state) {
        final Event event = getEvent(eventId);
        event.setState(state);
        if (state == EventState.PUBLISHED) {
            event.setPublished(LocalDateTime.now());
        }

        return mapper.toFullDto(event);
    }

    @Transactional
    @Override
    public RequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);
        if (!event.getRequestModeration()) {
            throw new ConflictException(
                    "The event does not require confirmation",
                    "request moderation is false");
        }

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ValidationException(
                    "Only initiator can confirm the request",
                    format("InitiatorId=%d", userId));
        }

        if (isMoreRequestLimit(event)) {
            throw new ConflictException(
                    "Request limit has been reached",
                    format("Request limit=%d", event.getParticipantLimit()));
        }

        Request request = event.getRequests().stream().filter(r -> Objects.equals(r.getId(), reqId)).findFirst()
                .orElseThrow(() -> new ValidationException(
                        "Wrong event for the request",
                        format("EventId=%d, RequestId=%d", eventId, reqId)));

        request.setStatus(CONFIRMED);

        if (isMoreRequestLimit(event)) {
            event.getRequests().removeIf(r -> r.getStatus() != CONFIRMED);
        }

        return requestMapper.toDto(request);
    }

    @Transactional
    @Override
    public RequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);

        if (!event.getRequestModeration()) {
            throw new ConflictException("The event does not require confirmation", "request moderation is false");
        }

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ValidationException("Only initiator can confirm the request", format("InitiatorId=%d", userId));
        }

        final Request request = event.getRequests().stream().filter(r -> Objects.equals(r.getId(), reqId)).findFirst()
                .orElseThrow(() -> new ValidationException("Wrong event for the request", format("EventId=%d, RequestId=%d", eventId, reqId)));

        event.getRequests().remove(request);
        request.setStatus(REJECTED);

        return requestMapper.toDto(request);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, UpdateEventDto eventDto) {
        Event event = getEvent(eventDto.getId());

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Published event can not be changed", EventState.PUBLISHED.toString());
        }

        if(!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    "Only initiator can update event",
                    format("InitiatorId=%d, UserId=%d", event.getInitiator().getId(), userId));
        }

        log.info("Update {} by {}", event, eventDto);
        mapper.update(eventDto, event);
        log.info("{} is ready to be updated", event);

        return mapper.toFullDto(event);
    }

    @Override
    public EventFullDto update(Long eventId, AdminUpdateEventDto eventDto) {
        Event event = getEvent(eventId);

        log.info("Update {} by {}", event, eventDto);
        mapper.update(eventDto, event);
        log.info("{} is ready to be updated", event);

        return mapper.toFullDto(event);
    }

    @Override
    public List<RequestDto> getRequests(Long userId, Long eventId) {
        final Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Only initiator can get requests", format("InitiatorId=%d", userId));
        }
        return requestMapper.toDto(event.getRequests());
    }

    @Override
    public boolean isMoreRequestLimit(Event event) {
        if (event.getParticipantLimit() == 0) {
            return false;
        }
        long confirmedRequestsCount = event.getRequests().stream().filter(r -> r.getStatus() == CONFIRMED).count();
        return confirmedRequestsCount >= event.getParticipantLimit();
    }

    public long getConfirmedRequests(Event event) {
        if (event.getRequests() == null) {
            return 0L;
        }
        return event.getRequests().stream().filter(r -> r.getStatus() == CONFIRMED).count();
    }

    //todo
    public long getViews(Event event) {
        return -1;
    }

    private Event getEvent(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Event is not found", format("Id=%d", id)));
    }
}
