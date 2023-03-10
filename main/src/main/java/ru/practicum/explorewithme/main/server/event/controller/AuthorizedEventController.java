package ru.practicum.explorewithme.main.server.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.dto.NewEventDto;
import ru.practicum.explorewithme.main.server.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.main.server.event.service.EventService;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusUpdateDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestStatusDto;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.util.validation.UserIdEqualsJwtSubConstraint;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedEventController {

    private final EventService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 1, jwtParamIndex = 2)
    public EventFullDto create(
            @Valid @RequestBody NewEventDto eventDto,
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Creating event with title={}", eventDto.getTitle());

        return service.create(userId, eventDto);
    }

    @GetMapping
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 3)
    public List<EventFullDto> getAll(
            @PathVariable Long userId,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Getting events");

        return service.getAll(userId, from, size);
    }

    @GetMapping("{eventId}")
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 2)
    public EventFullDto getById(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Getting event by ID");

        return service.getById(userId, eventId);
    }

    @PatchMapping("{eventId}")
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 1, jwtParamIndex = 3)
    public EventFullDto update(
            @RequestBody UpdateEventDto eventDto,
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Updating event by ID");

        return service.update(userId, eventId, eventDto);
    }

    @GetMapping("{eventId}/requests")
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 2)
    public List<ParticipationRequestDto> getRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Getting requests for event with id={}", eventId);

        return service.getRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 3)
    public ParticipationRequestStatusDto participationRequestUpdate(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody ParticipationRequestStatusUpdateDto requestStatusUpdateDto,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Getting requests for event with id={}", eventId);

        return service.participationRequestStatusUpdate(userId, eventId, requestStatusUpdateDto);
    }
}
