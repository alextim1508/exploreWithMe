package ru.practicum.explorewithme.mainServer.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.mainServer.event.model.Event;
import ru.practicum.explorewithme.mainServer.event.repository.EventRepository;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class EventFactoryImpl implements EventFactory {

    private final EventRepository repo;

    @Override
    public Event getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Event is not found", String.format("Id=%d", id)));
    }

}
