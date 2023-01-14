package ru.practicum.explorewithme.statsServer.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statsServer.stats.dto.EndpointHitsDto;
import ru.practicum.explorewithme.statsServer.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    @PostMapping("/hit")
    public void hit(@RequestBody EndpointHitsDto statsDto) {
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> get(@RequestParam(name = "uris", required = false) List<String> uris,
                                  @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
                                  @RequestParam(name = "start") LocalDateTime start,
                                  @RequestParam(name = "end") LocalDateTime end,
                                  @RequestParam(name = "app", defaultValue = "ewm-main-service") String appName) {
        return null;
    }
}
