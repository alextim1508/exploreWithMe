package ru.practicum.explorewithme.mainServer.compilation.service;

import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto create(NewCompilationDto compilation);

    List<CompilationDto> getAll(Integer from, Integer size, Boolean pinned);

    CompilationDto getById(Long compilationId);

    void pin(Long compilationId, boolean pinned);

    void addEvent(Long compilationId, Long eventId);

    void removeEvent(Long compilationId, Long eventId);

    void delete(Long compilationId);
}
