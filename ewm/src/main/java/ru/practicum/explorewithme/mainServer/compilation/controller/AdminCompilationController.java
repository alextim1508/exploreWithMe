package ru.practicum.explorewithme.mainServer.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.dto.NewCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    @PostMapping
    public CompilationDto create(@Valid @RequestBody NewCompilationDto compilationDto) {
        return null;
    }

    @DeleteMapping("{compilationId}")
    public void delete(@PathVariable Long compilationId) {
    }

    @PatchMapping("{compilationId}/pin")
    public void pin(@PathVariable Long compilationId) {
    }

    @DeleteMapping("{compilationId}/pin")
    public void unpin(@PathVariable Long compilationId) {
    }

    @PatchMapping("{compilationId}/events/{eventId}")
    public void addEvent(@PathVariable Long compilationId, @PathVariable Long eventId) {
    }

    @DeleteMapping("{compilationId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compilationId, @PathVariable Long eventId) {
    }
}
