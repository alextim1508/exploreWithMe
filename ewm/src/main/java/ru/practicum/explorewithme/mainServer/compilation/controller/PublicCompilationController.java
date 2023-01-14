package ru.practicum.explorewithme.mainServer.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size,
                                       @RequestParam(name = "pinned", required = false) Boolean pinned) {
        return null;
    }

    @GetMapping("{compilationId}")
    public CompilationDto get(@PathVariable Long compilationId) {
        return null;
    }
}
