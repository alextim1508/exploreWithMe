package ru.practicum.explorewithme.main.server.user.service;


import ru.practicum.explorewithme.main.server.user.model.User;

public interface UserFactory {

    void checkExisting(Long id);

    User getById(Long id);
}
