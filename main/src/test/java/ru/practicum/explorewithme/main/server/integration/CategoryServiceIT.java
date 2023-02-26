package ru.practicum.explorewithme.main.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.main.server.category.service.CategoryService;
import ru.practicum.explorewithme.main.server.exception.ConflictException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
class CategoryServiceIT {

    @Autowired
    CategoryService categoryService;

    NewCategoryDto categoryDto1;
    NewCategoryDto categoryDto2;

    @BeforeEach
    void setUp() {
        categoryDto1 = new NewCategoryDto("category1");
        categoryDto2 = new NewCategoryDto("category2");
    }

    @Test
    public void shouldSaveCategory() {
        List<CategoryDto> categories  = categoryService.getAll(0, 10);

        assertThat(categories)
                .isEmpty();

        CategoryDto savedCategory = categoryService.create(categoryDto1);

        CategoryDto categoryById  = categoryService.getById(savedCategory.getId());

        assertThat(categoryById)
                .isEqualTo(categoryById);
    }

    @Test
    public void shouldThrowExceptionWithDuplicateName() {
        categoryService.create(categoryDto1);

        categoryDto2.setName(categoryDto1.getName());

        assertThatThrownBy(() -> categoryService.create(categoryDto2))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void shouldUpdateCategory() {
        List<CategoryDto> categories  = categoryService.getAll(0, 10);

        assertThat(categories)
                .isEmpty();

        CategoryDto savedCategory = categoryService.create(categoryDto1);

        final String newName = "newName";
        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto(newName);

        categoryService.update(savedCategory.getId(), updateCategoryDto);


        CategoryDto categoryById = categoryService.getById(savedCategory.getId());

        assertThat(categoryById.getName())
                .isEqualTo(newName);
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void shouldDeleteUser() {
        CategoryDto savedCategory1 = categoryService.create(categoryDto1);
        CategoryDto savedCategory2 = categoryService.create(categoryDto2);

        categoryService.delete(savedCategory1.getId());

        assertThat(categoryService.getAll(0, 10))
                .hasSize(1)
                .containsAll(List.of(savedCategory2));
    }
}