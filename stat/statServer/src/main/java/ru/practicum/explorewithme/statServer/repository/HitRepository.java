package ru.practicum.explorewithme.statServer.repository;

import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository {

    Hit save(Hit hit);

    List<StatDto> getStats(
            String appName,
            LocalDateTime start,
            LocalDateTime end,
            Boolean uniqIp,
            @Nullable List<String> uris);
}
