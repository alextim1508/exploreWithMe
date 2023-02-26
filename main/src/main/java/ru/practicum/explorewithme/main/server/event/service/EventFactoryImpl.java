package ru.practicum.explorewithme.main.server.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.event.repository.EventRepository;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class EventFactoryImpl implements EventFactory {

    private final EventRepository repo;
    private final MessageSource messageSource;

    @Override
    public Event getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("event.not_found", new Object[]{id}, null),
                        String.format("eventId=%d", id)));
    }

}
