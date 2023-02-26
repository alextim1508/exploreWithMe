package ru.practicum.explorewithme.main.server.category.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.main.server.category.service.CategoryService;


import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(
            @Valid @RequestBody NewCategoryDto categoryDto) {

        log.debug("Creating category with name {}", categoryDto.getName());

        return service.create(categoryDto);
    }


    @PatchMapping("{categoryId}")
    public CategoryDto update(
            @Valid @RequestBody UpdateCategoryDto categoryDto,
            @Positive @PathVariable Long categoryId) {

        log.debug("Updating category with id {}", categoryId);

        return service.update(categoryId, categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{categoryId}")
    public void delete(
            @Positive @PathVariable Long categoryId) {

        log.debug("Deleting category with id {}", categoryId);

        service.delete(categoryId);
    }
}
