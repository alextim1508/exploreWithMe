package ru.practicum.explorewithme.stat.server.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.stat.dto.HitDto;
import ru.practicum.explorewithme.stat.server.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {

    @Mapping(target = "created", source = "timestamp")
    Hit toEntity(HitDto hitDto);

    @Mapping(target = "timestamp", source = "created")
    HitDto toDto(Hit hit);
}