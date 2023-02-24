package ru.practicum.explorewithme.main.server.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.aop.annotation.ControllerLog;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.repository.EventFilter;
import ru.practicum.explorewithme.main.server.event.service.EventService;
import ru.practicum.explorewithme.main.server.util.SortType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.main.server.event.model.EventState.PUBLISHED;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService service;

    @ControllerLog
    @GetMapping("{eventId}")
    public EventFullDto get(
            @PathVariable Long eventId,
            HttpServletRequest request) {

        return service.getPublishedById(eventId);
    }

    @ControllerLog
    @GetMapping
    public List<EventFullDto> getAll(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categoryIds,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) SortType sortType,
            HttpServletRequest request) {

        EventFilter eventFilter = EventFilter.builder()
                .text(text)
                .categoryIds(categoryIds)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .states(List.of(PUBLISHED))
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return service.getAll(eventFilter, sortType);
    }
}
