package ru.practicum.explorewithme.mainServer.event.service;

import ru.practicum.explorewithme.mainServer.event.model.Event;

public interface EventFactory {

    Event getById(Long id);
}
