package ru.practicum.explorewithme.main.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class AuthorizedRequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId) {

        log.debug("Creating event participation request for event with id {} by user with id {}", eventId, userId);

        return requestService.create(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAll(
            @PathVariable Long userId) {

        log.debug("Getting requests created by user with id {}", userId);

        return requestService.getAllByRequester(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancel(
            @PathVariable Long userId,
            @PathVariable Long requestId) {

        log.debug("Cancelling event participation request for request with id {} by user with id {}", requestId, userId);

        return requestService.cancel(userId, requestId);
    }
}
