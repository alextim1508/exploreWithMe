package ru.practicum.explorewithme.statServer.service;

import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.stat.dto.HitDto;
import ru.practicum.explorewithme.stat.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    HitDto create(HitDto hitDto);

    List<StatDto> get(
            String appName,
            LocalDateTime start,
            LocalDateTime end,
            Boolean uniqIp,
            @Nullable List<String> uris);
}
