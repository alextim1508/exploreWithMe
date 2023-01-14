package ru.practicum.explorewithme.mainServer.event.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class LocationDto {
    Double lat;
    Double lon;
}
