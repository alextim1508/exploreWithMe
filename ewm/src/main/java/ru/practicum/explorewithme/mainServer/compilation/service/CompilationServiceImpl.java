package ru.practicum.explorewithme.mainServer.compilation.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.mainServer.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.mainServer.compilation.model.Compilation;
import ru.practicum.explorewithme.mainServer.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.mainServer.event.model.Event;
import ru.practicum.explorewithme.mainServer.event.service.EventFactory;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.exception.ValidationException;
import ru.practicum.explorewithme.mainServer.util.OffsetLimitPageable;

import java.beans.Transient;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repo;
    private final CompilationMapper mapper;

    private final EventFactory eventFactory;

    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = mapper.toEntity(compilationDto);

        Compilation savedCompilation = repo.save(compilation);
        log.info("{} is saved", savedCompilation);

        return mapper.toDto(savedCompilation);
    }

    @Override
    public List<CompilationDto> getAll(Integer from, Integer size, @Nullable Boolean pinned) {
        Pageable pageable = OffsetLimitPageable.of(from, size);

        List<Compilation> compilations;
        if (pinned == null) {
            compilations = repo.findAllByPinned(pinned, pageable);
        } else {
            compilations = repo.findAll(pageable).getContent();
        }
        log.info("{} compilations are found", compilations.size());

        return mapper.toDto(compilations);
    }

    @Transient
    @Override
    public CompilationDto getById(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        log.info("{} is found", compilation);

        return mapper.toDto(compilation);
    }

    @Transient
    @Override
    public void pin(Long compilationId, boolean pinned) {
        Compilation compilation = getCompilation(compilationId);
        compilation.setPinned(pinned);

        log.info("Compilation with ID {} is {}", compilationId, pinned ? "pinned" : "unpinned");

        repo.save(compilation);
    }

    @Transient
    @Override
    public void addEvent(Long compilationId, Long eventId) {
        Compilation compilation = getCompilation(compilationId);

        Event event = eventFactory.getById(eventId);

        if (compilation.getEvents().contains(event)) {
            throw new ValidationException("Event is already in compilation", format("EventId=%d", eventId));
        }
        compilation.getEvents().add(event);

        log.info("Event with ID {} is added to compilation with ID{}", eventId, compilationId);

        repo.save(compilation);
    }

    @Transient
    @Override
    public void removeEvent(Long compilationId, Long eventId) {
        Compilation compilation = getCompilation(compilationId);

        Event event = eventFactory.getById(eventId);

        if (!compilation.getEvents().contains(event)) {
            throw new ValidationException("Event is not in compilation", format("EventId=%d", eventId));
        }
        compilation.getEvents().remove(event);

        log.info("Event with ID {} is removed from compilation with ID {}", eventId, compilationId);

        repo.save(compilation);
    }

    @Override
    public void delete(Long compilationId) {
        if(!repo.existsById(compilationId)) {
            throw new NotFoundException("Compilation is not found", format("Id=%d", compilationId));
        }

        repo.deleteById(compilationId);

        log.info("Compilation with ID {} is removed", compilationId);
    }

    private Compilation getCompilation(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation is not found", format("Id=%d", id)));
    }
}
