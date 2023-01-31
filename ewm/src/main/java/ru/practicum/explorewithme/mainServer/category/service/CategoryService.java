package ru.practicum.explorewithme.mainServer.category.service;


import ru.practicum.explorewithme.mainServer.category.dto.CategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(NewCategoryDto categoryDto);

    CategoryDto getById(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto update(UpdateCategoryDto categoryDto);

    void delete(Long id);
}
