package ru.practicum.explorewithme.mainServer.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;
import ru.practicum.explorewithme.mainServer.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class AuthorizedRequestController {

    private final RequestService service;

    @PostMapping
    public RequestDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId) {

        return service.create(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getAll(
            @PathVariable Long userId) {

        return service.getAllByRequester(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public RequestDto cancel(
            @PathVariable Long userId,
            @PathVariable Long requestId) {

        return service.cancel(userId, requestId);
    }
}
