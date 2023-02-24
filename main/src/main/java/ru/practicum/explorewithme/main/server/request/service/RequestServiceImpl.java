package ru.practicum.explorewithme.main.server.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.main.server.event.model.EventState;
import ru.practicum.explorewithme.main.server.event.service.EventService;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.exception.ValidationException;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.request.model.Request;
import ru.practicum.explorewithme.main.server.request.repository.RequestRepository;

import java.beans.Transient;
import java.util.List;

import static java.lang.String.format;
import static ru.practicum.explorewithme.main.server.request.model.RequestStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepo;
    private final RequestMapper requestMapper;

    private final EventService eventService;

    private final MessageSource messageSource;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        log.debug("Request to add participation request for event with id {} is received from user with id {}",
                eventId, userId);

        checkRequestIsNotRepeated(userId, eventId);

        Request request = requestMapper.toEntity(userId, eventId);

        checkRequesterIsNotInitiator(request);

        checkEventStatusIsPublished(request);

        checkSlot(request);

        if (request.getEvent().getRequestModeration() && request.getEvent().getParticipantLimit() != 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
        }

        Request savedRequest = requestRepo.save(request);

        log.debug("{} is added to repository", savedRequest);

        return requestMapper.toDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getAllByRequester(Long userId) {
        log.debug("List of requests created by user with id {} is requested", userId);

        List<Request> requesters = requestRepo.findAllByRequesterId(userId);

        log.debug("List of requests is received from repository with size of {}", requesters.size());

        return requestMapper.toDto(requesters);
    }

    @Transient
    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        log.debug("Request to cancel request with id {} is received from user with id {}", requestId, userId);

        Request request = getRequest(requestId);

        checkUserIsNotRequester(userId, request);

        checkEventStatusIsNotCanceled(request);

        request.setStatus(CANCELED);

        log.debug("Request with id {} is cancelled", request.getId());

        return requestMapper.toDto(request);
    }

    void checkSlot(Request request) {
        if (eventService.isMoreRequestLimit(request.getEvent())) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "request.event.limit_reached", null, null),
                    format("requestLimit=%d", request.getEvent().getParticipantLimit()));
        }
    }

    void checkEventStatusIsPublished(Request request) {
        if (request.getEvent().getState() != EventState.PUBLISHED) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "request.event.unpublished", null, null),
                    format("requestId=%d, eventState=%s", request.getId(), request.getEvent().getState()));
        }
    }

    void checkRequesterIsNotInitiator(Request request) {
        if (request.getRequester().getId().equals(request.getEvent().getInitiator().getId())) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "request.requester_is_initiator", null, null),
                    format("initiatorId=%d, requesterId=%d",
                            request.getEvent().getInitiator().getId(), request.getRequester().getId()));
        }
    }

    void checkRequestIsNotRepeated(Long userId, Long eventId) {
        requestRepo.findByRequesterIdAndEventId(userId, eventId)
                .ifPresent(r -> {
                    throw new ConflictException(
                            messageSource.getMessage(
                                    "request.repeated", null, null),
                            format("userId=%d, eventId=%d", r.getRequester().getId(), r.getEvent().getId()));
                });
    }

    void checkEventStatusIsNotCanceled(Request request) {
        if (request.getStatus().equals(CANCELED)) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "request.already_canceled", null, null),
                    String.format("requestId=%d, requestStatus=%s", request.getId(), request.getStatus()));
        }
    }

    void checkUserIsNotRequester(Long userId, Request request) {
        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException(
                    messageSource.getMessage(
                            "request.user.not_requester", new Object[]{request.getId(), userId}, null),
                    format("userId=%d, requesterId=%d ", userId, request.getRequester().getId()));
        }
    }

    Request getRequest(Long id) {
        return requestRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("event.not_found", new Object[]{id}, null),
                        format("requestId=%d", id)));
    }
}
