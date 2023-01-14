package ru.practicum.explorewithme.mainServer.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.util.SortType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {


    @GetMapping("{eventId}")
    public EventFullDto get(@PathVariable Long eventId, HttpServletRequest request) {
        return null;
    }

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(name = "text", required = false) String text,
                                       @RequestParam(name = "categories", required = false) List<Long> categories,
                                       @RequestParam(name = "paid", required = false) Boolean paid,
                                       @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                       @RequestParam(name = "sort", required = false) SortType sortType,
                                       @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                       @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size,
                                       HttpServletRequest request) {
        return null;
    }
}
