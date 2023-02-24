package ru.practicum.explorewithme.main.server.compilation.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.dto.UpdateCompilationDto;
import ru.practicum.explorewithme.main.server.compilation.model.Compilation;
import ru.practicum.explorewithme.main.server.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.util.OffsetLimitPageable;


import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepo;
    private final CompilationMapper compilationMapper;

    private final MessageSource messageSource;

    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        log.debug("Request to add compilation with title={} is received", compilationDto.getTitle());

        Compilation compilation = compilationMapper.toEntity(compilationDto);

        Compilation savedCompilation;
        try {
            savedCompilation = compilationRepo.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    messageSource.getMessage(
                        "compilation.title.duplicate", new Object[]{compilation.getTitle()}, null),
                        format("Name=%s", compilation.getTitle()));
        }

        log.info("{} is added to repository", savedCompilation);

        return compilationMapper.toDto(savedCompilation);
    }

    @Override
    public List<CompilationDto> getAll(Integer from, Integer size, @Nullable Boolean pinned) {
        log.debug("List of pinned={} compilations is requested with the following pagination parameters: " +
                        "from={} and size={}", pinned, from, size);

        Pageable pageable = OffsetLimitPageable.of(from, size);

        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepo.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepo.findAll(pageable).getContent();
        }

        log.debug("List of compilations is received from repository with size of {}", compilations.size());
        return compilationMapper.toDto(compilations);
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        log.debug("Compilation with compId {} is requested", compilationId);

        Compilation compilation = getCompilation(compilationId);

        log.debug("Compilation with id {} is received from repository", compilationId);

        return compilationMapper.toDto(compilation);
    }

    @Transient
    @Override
    public CompilationDto update(Long compilationId, UpdateCompilationDto compilationDto) {
        log.debug("Request to update compilation with id {} is received", compilationDto);

        Compilation compilation = getCompilation(compilationId);

        compilationMapper.update(compilationDto, compilation);

        Compilation savedCompilation;
        try {
            savedCompilation = compilationRepo.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    messageSource.getMessage(
                        "compilation.title.duplicate", new Object[]{compilation.getTitle()}, null),
                        format("Name=%s", compilation.getTitle()));
        }

        compilationRepo.save(compilation);

        log.debug("Compilation {} is updated in repository", compilationDto);

        return compilationMapper.toDto(savedCompilation);
    }


    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Request to delete compilation with id {} is received", id);

        Compilation compilation = getCompilation(id);

        if (compilation.getEvents().isEmpty()) {
            compilationRepo.deleteById(id);
        } else {
            throw new ConflictException(
                    messageSource.getMessage("compilation.delete.used_in_events", new Object[]{id}, null),
                    format("categoryId=%d, eventId=%s", id, compilation.getEvents().stream().map(event ->
                            String.valueOf(event.getId())).collect(Collectors.joining(", "))));
        }

        log.debug("Compilation with id {} is deleted from repository", id);
    }

    Compilation getCompilation(Long id) {
        return compilationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("compilation.not_found", new Object[]{id}, null),
                        format("Id=%d", id)));
    }
}
