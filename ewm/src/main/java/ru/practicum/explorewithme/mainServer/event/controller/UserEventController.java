package ru.practicum.explorewithme.mainServer.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.event.dto.NewEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {


    @PostMapping
    public EventFullDto create(@Valid @RequestBody NewEventDto eventDto, @PathVariable Long userId) {
        return null;
    }

    @PatchMapping
    public EventFullDto update(@RequestBody UpdateEventDto eventDto, @PathVariable Long userId) {
        return null;
    }

    @GetMapping
    public List<EventFullDto> getAll(@PathVariable Long userId,
                                     @RequestParam(name = "from", required = false) Integer from,
                                     @RequestParam(name = "size", required = false) Integer size) {
        return null;
    }

    @GetMapping("{eventId}")
    public EventFullDto get(@PathVariable Long userId, @PathVariable Long eventId) {
        return null;
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancel(@PathVariable Long userId, @PathVariable Long eventId) {
        return null;
    }

    @GetMapping("{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return null;
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public RequestDto confirm(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long reqId) {
        return null;
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public RequestDto reject(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @PathVariable Long reqId) {
        return null;
    }
}
