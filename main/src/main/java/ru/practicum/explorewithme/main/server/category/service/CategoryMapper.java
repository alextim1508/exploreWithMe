package ru.practicum.explorewithme.main.server.category.service;


import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.main.server.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);

    Category toEntity(NewCategoryDto categoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateCategoryDto categoryDto, @MappingTarget Category category);
}
