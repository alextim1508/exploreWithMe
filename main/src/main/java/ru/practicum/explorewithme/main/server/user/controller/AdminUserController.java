package ru.practicum.explorewithme.main.server.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;
import ru.practicum.explorewithme.main.server.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto create(
            @Valid @RequestBody NewUserDto userDto) {

        log.debug("Creating user with name {}", userDto.getName());

        return service.create(userDto);
    }

    @GetMapping
    public List<UserFullDto> get(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.debug("Getting users");

        return service.get(from, size, ids);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{userId}")
    public void delete(
            @Positive @PathVariable Long userId) {

        log.debug("Deleting user with id {}", userId);

        service.delete(userId);
    }
}
