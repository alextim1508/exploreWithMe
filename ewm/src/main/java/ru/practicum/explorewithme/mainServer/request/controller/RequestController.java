package ru.practicum.explorewithme.mainServer.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.request.dto.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    @PostMapping
    public RequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        return null;
    }

    @GetMapping
    public List<RequestDto> getAll(@PathVariable Long userId) {
        return null;
    }

    @PatchMapping("{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return null;
    }
}
