package ru.practicum.explorewithme.mainServer.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.event.dto.AdminUpdateEventDto;
import ru.practicum.explorewithme.mainServer.event.dto.EventFullDto;
import ru.practicum.explorewithme.mainServer.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(name = "users", required = false) List<Long> users,
                                     @RequestParam(name = "states", required = false) List<EventState> states,
                                     @RequestParam(name = "categories", required = false) List<Long> categories,
                                     @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                     @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return null;
    }

    @PutMapping("{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody AdminUpdateEventDto eventDto) {
       return null;
    }

    @PatchMapping("{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        return null;
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        return null;
    }

}
