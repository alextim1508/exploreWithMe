package ru.practicum.explorewithme.statServer.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.statServer.model.Hit;

@Mapper(componentModel = "spring")
public interface HitMapper {

    @Mapping(target = "created", source = "timestamp")
    Hit toEntity(HitDto hitDto);

    @Mapping(target = "timestamp", source = "created")
    HitDto toDto(Hit hit);
}