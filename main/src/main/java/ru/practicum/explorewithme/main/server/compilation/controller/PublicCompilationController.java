package ru.practicum.explorewithme.main.server.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.server.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getAll(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "pinned", required = false) Boolean pinned) {

        log.debug("Getting compilations");

        return service.getAll(from, size, pinned);
    }

    @GetMapping("{compilationId}")
    public CompilationDto get(
            @PathVariable Long compilationId) {

        log.debug("Getting compilation with id {}", compilationId);

        return service.getById(compilationId);
    }
}
