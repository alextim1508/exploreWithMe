package ru.practicum.explorewithme.statServer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stat.dto.HitDto;
import ru.practicum.explorewithme.stat.dto.StatDto;
import ru.practicum.explorewithme.statServer.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    public HitDto hit(
            @RequestBody HitDto hitDto) {

        return service.create(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> get(
            @RequestParam(name = "app") String appName,
            @RequestParam(name = "start") LocalDateTime start,
            @RequestParam(name = "end") LocalDateTime end,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
            @RequestParam(name = "uris", required = false) List<String> uris) {

        return service.get(appName, start, end, unique, uris);
    }
}
