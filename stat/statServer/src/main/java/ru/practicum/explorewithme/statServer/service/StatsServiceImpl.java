package ru.practicum.explorewithme.statServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;
import ru.practicum.explorewithme.statServer.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository repo;
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

        return repo.getStats(appName, start, end, uniqIp, uris);
    }
}
