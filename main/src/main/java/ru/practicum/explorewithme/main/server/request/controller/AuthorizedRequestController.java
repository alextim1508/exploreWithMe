package ru.practicum.explorewithme.main.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.request.service.RequestService;
import ru.practicum.explorewithme.main.server.util.validation.UserIdEqualsJwtSubConstraint;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedRequestController {

    private final RequestService requestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 2)
    public ParticipationRequestDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Creating event participation request for event with id {} by user with id {}", eventId, userId);

        return requestService.create(userId, eventId);
    }

    @GetMapping
    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 1)
    public List<ParticipationRequestDto> getAll(
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Getting requests created by user with id {}", userId);

        return requestService.getAllByRequester(userId);
    }

    @UserIdEqualsJwtSubConstraint(userIdParamIndex = 0, jwtParamIndex = 2)
    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancel(
            @PathVariable Long userId,
            @PathVariable Long requestId,
            @AuthenticationPrincipal Jwt jwt) {

        log.debug("Cancelling event participation request for request with id {} by user with id {}", requestId, userId);

        return requestService.cancel(userId, requestId);
    }
}
