package ru.practicum.explorewithme.main.server.compilation.service;

import ru.practicum.explorewithme.main.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto compilation);

    List<CompilationDto> getAll(Integer from, Integer size, Boolean pinned);

    CompilationDto getById(Long compilationId);

    CompilationDto update(Long compilationId, UpdateCompilationDto compilationDto);

    void delete(Long compilationId);
}
