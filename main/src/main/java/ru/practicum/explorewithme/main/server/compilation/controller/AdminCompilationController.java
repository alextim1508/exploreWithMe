package ru.practicum.explorewithme.main.server.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.UpdateCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto create(
            @Valid @RequestBody NewCompilationDto compilationDto) {

        log.debug("Creating compilation with title {}", compilationDto.getTitle());

        return service.create(compilationDto);
    }


    @PatchMapping("/{compilationId}")
    public void update(
            @PathVariable Long compilationId,
            @Valid @RequestBody UpdateCompilationDto compilationDto) {

        log.debug("Updating compilation with id {}", compilationId);

        service.update(compilationId, compilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{compilationId}")
    public void delete(
            @PathVariable Long compilationId) {

        log.debug("Deleting compilation with id {}", compilationId);

        service.delete(compilationId);
    }
}
