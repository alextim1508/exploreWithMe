package ru.practicum.explorewithme.mainServer.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.user.dto.NewUserDto;
import ru.practicum.explorewithme.mainServer.user.dto.UserFullDto;
import ru.practicum.explorewithme.mainServer.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService service;

    @PostMapping
    public UserFullDto create(
            @Valid @RequestBody NewUserDto userDto) {

        return service.create(userDto);
    }

    @GetMapping
    public List<UserFullDto> get(
            @RequestParam(name = "ids", required = false) List<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return service.get(from, size, ids);
    }

    @DeleteMapping("{userId}")
    public void delete(
            @PathVariable Long userId) {

        service.delete(userId);
    }
}
