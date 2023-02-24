package ru.practicum.explorewithme.main.server.category.service;


import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto categoryDto);

    CategoryDto getById(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto update(Long categoryId, UpdateCategoryDto categoryDto);

    void delete(Long id);
}
