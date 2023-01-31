package ru.practicum.explorewithme.mainServer.event.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.mainServer.event.dto.LocationDto;
import ru.practicum.explorewithme.mainServer.event.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "latitude", target = "lat")
    @Mapping(source = "longitude", target = "lon")
    LocationDto toDto(Location location);

    @Mapping(source = "lat", target = "latitude")
    @Mapping(source = "lon", target = "longitude")
    Location toEntity(LocationDto locationDto);
}
