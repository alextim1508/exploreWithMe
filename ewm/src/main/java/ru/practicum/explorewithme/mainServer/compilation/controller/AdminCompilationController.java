package ru.practicum.explorewithme.mainServer.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService service;

    @PostMapping
    public CompilationDto create(
            @Valid @RequestBody NewCompilationDto compilationDto) {

        return service.create(compilationDto);
    }

    @PatchMapping("{compilationId}/pin")
    public void pin(
            @PathVariable Long compilationId) {

        service.pin(compilationId, true);
    }

    @DeleteMapping("{compilationId}/pin")
    public void unpin(
            @PathVariable Long compilationId) {

        service.pin(compilationId, false);
    }

    @PatchMapping("{compilationId}/events/{eventId}")
    public void addEvent(
            @PathVariable Long compilationId,
            @PathVariable Long eventId) {

        service.addEvent(compilationId, eventId);
    }

    @DeleteMapping("{compilationId}/events/{eventId}")
    public void removeEvent(
            @PathVariable Long compilationId,
            @PathVariable Long eventId) {

        service.removeEvent(compilationId, eventId);
    }

    @DeleteMapping("{compilationId}")
    public void delete(
            @PathVariable Long compilationId) {

        service.delete(compilationId);
    }
}
