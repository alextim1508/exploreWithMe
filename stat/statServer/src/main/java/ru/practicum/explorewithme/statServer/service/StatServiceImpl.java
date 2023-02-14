package ru.practicum.explorewithme.statServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.core.dto.HitDto;
import ru.practicum.explorewithme.core.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;
import ru.practicum.explorewithme.statServer.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository repo;

    private final HitMapper mapper;

    @Override
    public HitDto create(HitDto hitDto) {
        Hit hit = mapper.toEntity(hitDto);

        Hit savedHit = repo.save(hit);
        log.info("{} is saved", savedHit);

        return mapper.toDto(savedHit);
    }

    @Override
    public List<StatDto> get(
            String appName,
            LocalDateTime start,
            LocalDateTime end,
            Boolean uniqIp,
            @Nullable List<String> uris) {

        List<StatDto> stats;
        if (uris == null && !uniqIp) {
            stats = repo.findByCreated(appName, start, end);
        } else if (uris == null) {
            stats = repo.findByCreatedWithUniqIp(appName, start, end);
        } else if (!uniqIp) {
            stats = repo.findByCreatedAndUri(appName, start, end, uris);
        } else {
            stats = repo.findByCreatedAndUriWithUniqIp(appName, start, end, uris);
        }

        log.info("{} StatDto are found", stats.size());

        return stats.stream()
                .sorted(Comparator.comparingLong(StatDto::getHits).reversed())
                .collect(Collectors.toList());

    }
}
