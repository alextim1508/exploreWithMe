package ru.practicum.explorewithme.main.server.event.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.main.server.event.dto.LocationDto;
import ru.practicum.explorewithme.main.server.event.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "latitude", target = "lat")
    @Mapping(source = "longitude", target = "lon")
    LocationDto toDto(Location location);

    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    Location toEntity(LocationDto locationDto);
}
