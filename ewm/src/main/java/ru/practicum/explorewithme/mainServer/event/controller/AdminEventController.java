package ru.practicum.explorewithme.mainServer.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.event.model.EventState;
import ru.practicum.explorewithme.mainServer.event.repository.EventFilter;
import ru.practicum.explorewithme.mainServer.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService service;

    @GetMapping
    public List<EventFullDto> getAll(
            @RequestParam(name = "users", required = false) List<Long> userIds,
            @RequestParam(name = "categories", required = false) List<Long> categoryIds,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "rangeStart", required = false)  LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)  LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        EventFilter eventFilter = EventFilter.builder()
                .userIds(userIds)
                .categoryIds(categoryIds)
                .states(states)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return service.getAll(eventFilter);
    }

    @PutMapping("{eventId}")
    public EventFullDto update(
            @PathVariable Long eventId, @RequestBody AdminUpdateEventDto eventDto) {

        return service.update(eventId, eventDto);
    }

    @PatchMapping("{eventId}/publish")
    public EventFullDto publish(
            @PathVariable Long eventId) {

        return service.setEventState(eventId, EventState.PUBLISHED);
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto reject(
            @PathVariable Long eventId) {

        return service.setEventState(eventId, EventState.CANCELED);
    }

}
