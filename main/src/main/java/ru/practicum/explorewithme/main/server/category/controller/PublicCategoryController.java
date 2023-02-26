package ru.practicum.explorewithme.main.server.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getAll(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.debug("Getting categories");

        return service.getAll(from, size);
    }

    @GetMapping("{categoryId}")
    public CategoryDto get(
            @PathVariable Long categoryId) {

        log.debug("Getting category with id {}", categoryId);

        return service.getById(categoryId);
    }
}
