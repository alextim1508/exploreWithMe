package ru.practicum.explorewithme.main.server.user.service;

import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;

import java.util.List;

public interface UserService {

    UserFullDto create(NewUserDto user);

    List<UserFullDto> get(Integer from, Integer size, List<Long> ids);

    void delete(Long id);
}
