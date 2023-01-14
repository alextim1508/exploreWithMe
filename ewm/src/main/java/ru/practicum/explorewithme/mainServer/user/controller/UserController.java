package ru.practicum.explorewithme.mainServer.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.user.dto.NewUserDto;
import ru.practicum.explorewithme.mainServer.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserDto userDto) {
        return null;
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(name = "ids", required = false) List<Long> ids,
                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return null;
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable Long userId) {
    }
}
