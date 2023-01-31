package ru.practicum.explorewithme.mainServer.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.mainServer.event.model.EventState;
import ru.practicum.explorewithme.mainServer.event.service.EventService;
import ru.practicum.explorewithme.mainServer.exception.ConflictException;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.exception.ValidationException;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;
import ru.practicum.explorewithme.mainServer.request.model.Request;
import ru.practicum.explorewithme.mainServer.request.model.RequestStatus;
import ru.practicum.explorewithme.mainServer.request.repository.RequestRepository;

import java.beans.Transient;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repo;
    private final RequestMapper mapper;

    private final EventService eventService;

    @Override
    public RequestDto create(Long userId, Long eventId) {
        repo.findByRequesterIdAndEventId(userId, eventId)
                .ifPresent(r -> {
                    throw new ConflictException("Request already exists",
                            format("UserId=%d, EventId=%d", r.getRequester().getId(), r.getEvent().getId()));
                });

        Request request = mapper.toEntity(userId, eventId);

        if (request.getRequester().getId().equals(request.getEvent().getInitiator().getId())) {
            throw new ConflictException(
                    "Participation is not available for initiator",
                    format("InitiatorId=%d", request.getEvent().getInitiator().getId()));
        }

        if (request.getEvent().getState() != EventState.PUBLISHED) {
            throw new ConflictException(
                    "Event is not published yet",
                    format("Event state=%s", request.getEvent().getState().toString()));
        }

        if (eventService.isMoreRequestLimit(request.getEvent())) {
            throw new ConflictException(
                    "Request limit has been reached",
                    format("Request limit=%d", request.getEvent().getParticipantLimit()));
        }

        if (request.getEvent().getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        Request savedRequest = repo.save(request);

        log.info("{} is saved", savedRequest);

        return mapper.toDto(savedRequest);
    }

    @Override
    public List<RequestDto> getAllByRequester(Long userId) {
        List<Request> requesters = repo.findAllByRequesterId(userId);
        log.info("{} requesters are found", requesters.size());

        return mapper.toDto(requesters);
    }

    @Transient
    @Override
    public RequestDto cancel(Long userId, Long requestId) {
        Request request = getRequest(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException("Only requester can cancel request", format("UserId=%d", userId));
        }
        request.setStatus(RequestStatus.CANCELED);
        log.info("{} is canceled by user with ID {}", request, userId);

        return mapper.toDto(request);
    }

    private Request getRequest(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found", format("Id=%d", id)));
    }
}
