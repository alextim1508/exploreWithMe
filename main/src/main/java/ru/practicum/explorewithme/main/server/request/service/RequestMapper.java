package ru.practicum.explorewithme.main.server.request.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.main.server.event.service.EventFactory;
import ru.practicum.explorewithme.main.server.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.server.request.model.Request;
import ru.practicum.explorewithme.main.server.user.service.UserFactoryImpl;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserFactoryImpl.class, EventFactory.class}
)
public interface RequestMapper {

    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "event", source = "event.id")
    ParticipationRequestDto toDto(Request request);

    List<ParticipationRequestDto> toDto(List<Request> requests);

    @Mapping(target = "requester", source = "requesterId")
    @Mapping(target = "event", source = "eventId")
    Request toEntity(Long requesterId, Long eventId);
}
