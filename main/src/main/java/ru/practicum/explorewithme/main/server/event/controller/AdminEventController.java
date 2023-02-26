package ru.practicum.explorewithme.main.server.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.model.EventState;
import ru.practicum.explorewithme.main.server.event.repository.EventFilter;
import ru.practicum.explorewithme.main.server.event.service.EventService;

import javax.validation.Valid;
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

    @PatchMapping("{eventId}")
    public EventFullDto update(
            @PathVariable Long eventId, @Valid @RequestBody AdminUpdateEventDto eventDto) {

        return service.update(eventId, eventDto);
    }



}
