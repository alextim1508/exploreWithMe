package ru.practicum.explorewithme.mainServer.user.service;

import ru.practicum.explorewithme.mainServer.user.model.User;

public interface UserFactory {

    User getById(Long id);
}
