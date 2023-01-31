package ru.practicum.explorewithme.mainServer.category.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.category.dto.CategoryDto;
import ru.practicum.explorewithme.mainServer.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getAll(
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        return service.getAll(from, size);
    }

    @GetMapping("{categoryId}")
    public CategoryDto get(
            @PathVariable Long categoryId) {

        return service.getById(categoryId);
    }
}
