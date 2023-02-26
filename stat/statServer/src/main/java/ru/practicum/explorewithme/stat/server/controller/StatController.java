package ru.practicum.explorewithme.stat.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stat.dto.HitDto;
import ru.practicum.explorewithme.stat.dto.StatDto;
import ru.practicum.explorewithme.stat.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {

    private final StatService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public HitDto hit(
            @RequestBody HitDto hitDto) {

        log.debug("Creating hit {}", hitDto);

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
