package ru.practicum.explorewithme.main.server.event.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.dto.NewEventDto;
import ru.practicum.explorewithme.main.server.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.event.model.EventState;
import ru.practicum.explorewithme.main.server.event.repository.EventFilter;
import ru.practicum.explorewithme.main.server.event.repository.EventRepository;
import ru.practicum.explorewithme.main.server.event.repository.EventSpecification;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.exception.ValidationException;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusUpdateDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.request.model.Request;
import ru.practicum.explorewithme.main.server.request.model.RequestStatus;
import ru.practicum.explorewithme.main.server.request.service.RequestMapper;
import ru.practicum.explorewithme.main.server.user.service.UserFactory;
import ru.practicum.explorewithme.main.server.util.OffsetLimitPageable;
import ru.practicum.explorewithme.main.server.util.SortType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.practicum.explorewithme.main.server.event.dto.AdminStateAction.PUBLISH_EVENT;
import static ru.practicum.explorewithme.main.server.event.dto.AdminStateAction.REJECT_EVENT;
import static ru.practicum.explorewithme.main.server.event.model.EventState.*;
import static ru.practicum.explorewithme.main.server.request.model.RequestStatus.CONFIRMED;
import static ru.practicum.explorewithme.main.server.request.model.RequestStatus.REJECTED;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepo;
    private final EventMapper eventMapper;

    private final UserFactory userFactory;

    private final RequestMapper requestMapper;

    private final MessageSource messageSource;

    @Value("${app.event_delta_hours}")
    private int hoursDelta;

    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        log.debug("Request to add event {} is received", eventDto);

        checkEventDateLeastComeHoursFromNowInFuture(eventDto.getEventDate());

        Event event = eventMapper.toEntity(eventDto, userId);

        event.setState(EventState.PENDING);

        Event savedEvent = eventRepo.save(event);

        log.debug("Event {} is added to repository", savedEvent);

        return eventMapper.toFullDto(savedEvent);
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        log.debug("Event with id {} is requested by user with id {}", eventId, userId);

        userFactory.checkExisting(userId);

        Event event = eventRepo.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("event.not_found", new Object[]{eventId}, null),
                        format("eventId=%d, initiatorId=%d", eventId, userId)));

        log.info("{} is received from repository", event);

        return eventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto getPublishedById(Long eventId) {
        log.debug("Published event with id {} is requested", eventId);

        Event event = eventRepo.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("event.not_found", new Object[]{eventId}, null),
                        format("Id=%d", eventId)));

        log.info("{} is received from repository", event);

        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(Long userId, Integer from, Integer size) {
        log.debug("List of events initiated by user with id={} is requested with the following pagination parameters: " +
                "from={} and size={}.", userId, from, size);

        List<Event> events = eventRepo.findAllByInitiatorId(userId, OffsetLimitPageable.of(from, size));

        log.debug("Lst of events is received from repository with size of {}", events.size());

        return eventMapper.toFullDto(events);
    }

    @Override
    public List<EventFullDto> getAll(EventFilter eventFilter) {
        log.debug("List of events initiated is requested with filter: {}", eventFilter);

        List<Event> events = eventRepo.findAll(
                        new EventSpecification(eventFilter),
                        OffsetLimitPageable.of(eventFilter.getFrom(), eventFilter.getSize()))
                .getContent();

        log.debug("Lst of events is received from repository with size of {}", events.size());

        return eventMapper.toFullDto(events);
    }

    @Override
    public List<EventFullDto> getAll(EventFilter eventFilter, SortType sortType) {
        log.debug("List of events initiated is requested with {} and {}", eventFilter, sortType);

        List<Event> events = eventRepo.findAll(
                        new EventSpecification(eventFilter),
                        OffsetLimitPageable.of(eventFilter.getFrom(), eventFilter.getSize()))
                .getContent();

        if (eventFilter.getOnlyAvailable() != null && eventFilter.getOnlyAvailable()) {
            events = events.stream().filter(e -> !isMoreRequestLimit(e)).collect(Collectors.toList());
        }

        log.debug("Lst of events is received from repository with size of {}", events.size());

        return eventMapper.toFullDto(events, sortType);
    }

    @Override
    public ParticipationRequestStatusDto participationRequestStatusUpdate(
            Long userId,
            Long eventId,
            ParticipationRequestStatusUpdateDto requestStatusUpdateDto) {

        log.debug("A request to update requests for event with id={} initiated by user with id={} is received",
                eventId, userId);

        userFactory.checkExisting(userId);

        ParticipationRequestStatusDto res = new ParticipationRequestStatusDto(new ArrayList<>(), new ArrayList<>());

        RequestStatus status = requestStatusUpdateDto.getStatus();

        for (Long id : requestStatusUpdateDto.getRequestIds()) {
            if (status == CONFIRMED) {
                res.getConfirmedRequests().add(confirm(userId, eventId, id));
            } else if (status == REJECTED) {
                res.getRejectedRequests().add(reject(userId, eventId, id));
            }
        }

        log.debug("Request status update result object is received");
        return res;
    }

    @Transactional
    @Override
    public ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);

        checkEventIsModerated(event);

        checkUserIsInitiator(userId, event);

        checkSlot(event);

        Request request = getRequest(event, reqId);

        request.setStatus(CONFIRMED);

        eventRepo.save(event);

        return requestMapper.toDto(request);
    }

    @Transactional
    @Override
    public ParticipationRequestDto reject(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);

        checkEventIsModerated(event);

        checkUserIsInitiator(userId, event);

        Request request = getRequest(event, reqId);

        checkRequestStatusIsNotConfirmed(request);

        request.setStatus(REJECTED);

        eventRepo.save(event);

        return requestMapper.toDto(request);
    }

    Request getRequest(Event event, Long reqId) {
        Request request = event.getRequests().stream().filter(r -> Objects.equals(r.getId(), reqId)).findFirst()
                .orElseThrow(() ->
                        new ValidationException(
                                messageSource.getMessage("event.request.not_found_by_initiator",
                                        new Object[] {reqId}, null),
                                format("eventId=%d, requestId=%d", event.getId(), reqId)));
        return request;
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventDto eventDto) {
        log.debug("Request to update event with id {} is received from user with id {}", eventId, userId);

        Event event = getEvent(eventId);

        checkUserIsInitiator(userId, event);

        checkEventStatusIsNotPublished(event);

        checkEventDateLeastComeHoursFromNowInFuture(eventDto.getEventDate());

        eventMapper.update(eventDto, event);

        log.info("{} is ready to be updated", event);

        return eventMapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto update(Long eventId, AdminUpdateEventDto eventDto) {
        log.debug("Request to update event with id {} is received", eventId);

        Event event = getEvent(eventId);

        checkEventStatusIsNotPendingWhenTryingToPublish(eventDto, event);

        checkEventStatusIsNotPublishedWhenTryingToReject(eventDto, event);

        checkEventDateLeastComeHoursFromNowInFuture(eventDto.getEventDate());

        eventMapper.update(eventDto, event);

        log.info("{} is ready to be updated", event);

        return eventMapper.toFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        log.debug("List of requests created for event with id {} initiated by user with id {} is requested",
                eventId, userId);

        final Event event = getEvent(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    messageSource.getMessage("event.requests.not_initiator", new Object[]{userId}, null),
                    format("userId=%d, initiatorId=%d, eventId=%d", userId, event.getInitiator().getId(), event.getId()));
        }

        log.debug("List of requests is received from repository with size of {}", event.getRequests().size());

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

    private void checkRequestStatusIsNotConfirmed(Request request) {
        if (request.getStatus() == CONFIRMED)
            throw new ConflictException(
                    messageSource.getMessage("event.request.status.confirmed", null, null),
                    String.format("requestStatus=%s", request.getStatus()));
    }

    void checkSlot(Event event) {
        if (isMoreRequestLimit(event)) {
            throw new ConflictException(
                    messageSource.getMessage("event.request.state.participant_limit", null, null),
                    format("Request limit=%d", event.getParticipantLimit()));
        }
    }

    void checkUserIsInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(
                    messageSource.getMessage("event.request.state.not_initiator", null, null),
                    format("InitiatorId=%d", userId));
        }
    }

    void checkEventIsModerated(Event event) {
        if (!event.getRequestModeration()) {
            throw new ConflictException(
                    messageSource.getMessage(
                        "event.request.state.not_moderation", null, null),
                        "request moderation is false");
        }
    }

    void checkEventDateLeastComeHoursFromNowInFuture(LocalDateTime dateTime) {
        if (dateTime != null) {
            if (LocalDateTime.now().plusHours(hoursDelta).isAfter(dateTime)) {
                throw new ConflictException(
                        messageSource.getMessage("event.date.not_valid", null, null),
                        String.format("eventDate=%s", dateTime));
            }
        }
    }

    void checkEventStatusIsNotPublishedWhenTryingToReject(AdminUpdateEventDto eventDto, Event event) {
        if (eventDto.getStateAction() == REJECT_EVENT && event.getState() == PUBLISHED) {
            throw new ConflictException(
                    messageSource.getMessage("event.state.reject.published", null, null),
                    String.format("eventState=%s", event.getState()));
        }
    }

    void checkEventStatusIsNotPendingWhenTryingToPublish(AdminUpdateEventDto eventDto, Event event) {
        if (eventDto.getStateAction() == PUBLISH_EVENT && event.getState() != PENDING) {
            throw new ConflictException(
                    messageSource.getMessage("event.state.publish.not_pending", null, null),
                    String.format("eventState=%s", event.getState()));
        }
    }

    void checkEventStatusIsNotPublished(Event event) {
        if (event.getState() == PUBLISHED) {
            throw new ConflictException(
                    messageSource.getMessage("event.state.change.published", null, null),
                    PUBLISHED.toString());
        }
    }

    Event getEvent(Long id) {
        return eventRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("event.not_found", new Object[]{id}, null),
                        format("eventId=%d", id)));
    }
}
