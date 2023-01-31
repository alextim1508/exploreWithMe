package ru.practicum.explorewithme.mainServer.event.repository;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.mainServer.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class EventFilter {

    private List<Long> userIds;

    private List<Long> categoryIds;

    private List<EventState> states;

    private String text;

    private Boolean paid;

    private Boolean onlyAvailable;

    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;

    private Integer from;
    private Integer size;
}
