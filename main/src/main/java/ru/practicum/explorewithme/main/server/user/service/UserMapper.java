package ru.practicum.explorewithme.main.server.user.service;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;
import ru.practicum.explorewithme.main.server.user.dto.UserShortDto;
import ru.practicum.explorewithme.main.server.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserShortDto toShortDto(User user);

    UserFullDto toFullDto(User user);

    List<UserFullDto> toFullDto(List<User> users);

    User toEntity(NewUserDto userDto);
}
