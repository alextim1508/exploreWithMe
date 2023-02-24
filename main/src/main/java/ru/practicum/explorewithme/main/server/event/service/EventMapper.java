package ru.practicum.explorewithme.main.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.explorewithme.main.server.category.service.CategoryFactory;
import ru.practicum.explorewithme.main.server.event.dto.*;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.event.model.EventState;
import ru.practicum.explorewithme.main.server.user.service.UserFactoryImpl;
import ru.practicum.explorewithme.main.server.util.SortType;
import ru.practicum.explorewithme.stat.client.StatClient;
import ru.practicum.explorewithme.stat.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.practicum.explorewithme.main.server.event.dto.StateAction.CANCEL_REVIEW;
import static ru.practicum.explorewithme.main.server.event.dto.StateAction.SEND_TO_REVIEW;
import static ru.practicum.explorewithme.main.server.event.dto.AdminStateAction.PUBLISH_EVENT;
import static ru.practicum.explorewithme.main.server.event.dto.AdminStateAction.REJECT_EVENT;
import static ru.practicum.explorewithme.main.server.request.model.RequestStatus.CONFIRMED;


@Mapper(
        componentModel = "spring",
        uses = {UserFactoryImpl.class, CategoryFactory.class, LocationMapper.class}
)
@Slf4j
public abstract class EventMapper {

    @Autowired
    private StatClient statClient;

    @Value("base-event-url")
    private String baseEventUrl;

    @Value("${spring.mvc.format.date-time}")
    private String dateTimeFormat;


    public abstract EventShortDto toShortDto(Event event);

    @Mapping(target = "publishedOn", source = "event.published")
    @Mapping(target = "createdOn", source = "event.created")
    @Mapping(target = "views", expression = "java(getViews(event))")
    @Mapping(target = "confirmedRequests", expression = "java(getConfirmedRequests(event))")
    public abstract EventFullDto toFullDto(Event event);

    @Mapping(target = "views", expression = "java(getViews(event))")
    @Mapping(target = "confirmedRequests", expression = "java(getConfirmedRequests(event))")
    public abstract List<EventFullDto> toFullDto(List<Event> events);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "userId", target = "initiator")
    @Mapping(source = "eventDto.categoryId", target = "category")
    public abstract Event toEntity(NewEventDto eventDto, Long userId);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "state", expression = "java(getEventStateByStateAction(eventDto))")
    public abstract void update(UpdateEventDto eventDto, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "state", expression = "java(getEventStateByStateAction(eventDto))")
    public abstract void update(AdminUpdateEventDto eventDto, @MappingTarget Event event);


    public List<EventFullDto> toFullDto(List<Event> events, SortType sortType) {
        if (Objects.isNull(sortType)) {
            return toFullDto(events);
        }

        Comparator<EventFullDto> comparator = Comparator.comparing(EventFullDto::getEventDate);
        if (sortType == SortType.VIEWS) {
            comparator = Comparator.comparing(EventFullDto::getViews);
        }

        List<EventFullDto> eventDtos = toFullDto(events);
        eventDtos.sort(comparator);

        return eventDtos;
    }

    public EventState getEventStateByStateAction(UpdateEventDto eventDto) {
        if (eventDto.getStateAction() == SEND_TO_REVIEW) {
            return EventState.PENDING;
        }

        if (eventDto.getStateAction() == CANCEL_REVIEW) {
            return EventState.CANCELED;
        }

        return null;
    }

    public EventState getEventStateByStateAction(AdminUpdateEventDto eventDto) {
        if (eventDto.getStateAction() == PUBLISH_EVENT) {
            return EventState.PUBLISHED;
        }

        if (eventDto.getStateAction() == REJECT_EVENT) {
            return EventState.REJECTED;
        }

        return null;
    }

    public long getConfirmedRequests(Event event) {
        if (event.getRequests() == null) {
            return 0L;
        }

        return event.getRequests().stream().filter(r -> r.getStatus() == CONFIRMED).count();
    }

    public long getViews(Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        Map<String, String> parameters = new HashMap<>();
        String uri = String.format("%s/%d", baseEventUrl, event.getId());
        parameters.put("uris", uri);
        parameters.put("start", event.getCreated().format(formatter));
        parameters.put("end", LocalDateTime.now().format(formatter));
        try {
            StatDto hits = statClient.get(parameters).stream()
                    .filter(h -> uri.equals(h.getUri())).findFirst().orElse(null);
            if (hits == null) {
                return 0;
            } else {
                return hits.getHits();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return 0;
    }
}
