package ru.practicum.explorewithme.mainServer.request.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.mainServer.event.service.EventFactory;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;
import ru.practicum.explorewithme.mainServer.request.model.Request;
import ru.practicum.explorewithme.mainServer.user.service.UserFactoryImpl;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserFactoryImpl.class, EventFactory.class}
)
public interface RequestMapper {

    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "event", source = "event.id")
    RequestDto toDto(Request request);

    List<RequestDto> toDto(List<Request> requests);

    @Mapping(target = "requester", source = "requesterId")
    @Mapping(target = "event", source = "eventId")
    Request toEntity(Long requesterId, Long eventId);
}
