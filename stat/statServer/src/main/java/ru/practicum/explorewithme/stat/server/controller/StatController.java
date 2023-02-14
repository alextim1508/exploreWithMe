package ru.practicum.explorewithme.stat.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stat.dto.HitDto;
import ru.practicum.explorewithme.stat.dto.StatDto;
import ru.practicum.explorewithme.stat.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    public HitDto hit(
            @RequestBody HitDto hitDto) {

        return service.create(hitDto);
    }

    @GetMapping("/stats")
    public List<StatDto> get(
            @RequestParam(name = "app", defaultValue = "${app.default-app-name}") String appName,
            @RequestParam(name = "start") LocalDateTime start,
            @RequestParam(name = "end") LocalDateTime end,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
            @RequestParam(name = "uris", required = false) List<String> uris) {

        return service.get(appName, start, end, unique, uris);
    }
}
