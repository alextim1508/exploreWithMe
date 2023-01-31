package ru.practicum.explorewithme.mainServer.event.service;

import org.mapstruct.*;
import ru.practicum.explorewithme.mainServer.category.service.CategoryFactory;
import ru.practicum.explorewithme.mainServer.event.dto.*;
import ru.practicum.explorewithme.mainServer.event.model.Event;
import ru.practicum.explorewithme.mainServer.user.service.UserFactoryImpl;

import java.util.List;


@Mapper(
        componentModel = "spring",
        uses = {UserFactoryImpl.class, CategoryFactory.class, LocationMapper.class}
)
public interface EventMapper {

    EventShortDto toShortDto(Event event);

    @Mapping(target = "publishedOn", source = "event.published")
    @Mapping(target = "createdOn", source = "event.created")
    EventFullDto toFullDto(Event event);

    List<EventFullDto> toFullDto(List<Event> events);

    @Mapping(source = "userId", target = "initiator")
    @Mapping(source = "eventDto.categoryId", target = "category")
    Event toEntity(NewEventDto eventDto, Long userId);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateEventDto eventDto, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(AdminUpdateEventDto eventDto, @MappingTarget Event event);

}
