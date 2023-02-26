package ru.practicum.explorewithme.main.server.event.service;

import ru.practicum.explorewithme.main.server.event.model.Event;

public interface EventFactory {

    Event getById(Long id);
}
