package ru.practicum.explorewithme.mainServer.category.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.mainServer.category.dto.CategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.mainServer.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    public CategoryDto create(
            @Valid @RequestBody NewCategoryDto categoryDto) {

        return service.create(categoryDto);
    }

    @PatchMapping
    public CategoryDto update(
            @Valid @RequestBody UpdateCategoryDto categoryDto) {

        return service.update(categoryDto);
    }

    @DeleteMapping("{categoryId}")
    public void delete(
            @PathVariable Long categoryId) {

        service.delete(categoryId);
    }
}
