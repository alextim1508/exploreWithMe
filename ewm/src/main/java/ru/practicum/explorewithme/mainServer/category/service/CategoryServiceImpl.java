package ru.practicum.explorewithme.mainServer.category.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.mainServer.category.dto.CategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.mainServer.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.mainServer.category.model.Category;
import ru.practicum.explorewithme.mainServer.category.repository.CategoryRepository;
import ru.practicum.explorewithme.mainServer.exception.ConflictException;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.util.OffsetLimitPageable;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto categoryDto) {
        Category category = mapper.toEntity(categoryDto);

        Category savedCategory;
        try {
            savedCategory = repo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name is already in use", format("Name=%s", category.getName()));
        }
        log.info("{} is saved", savedCategory);

        return mapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = getCategory(id);
        log.info("{} is found", category);

        return mapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        List<Category> categories = repo.findAll(OffsetLimitPageable.of(from, size)).getContent();
        log.info("{} categories are found", categories.size());

        return mapper.toDto(categories);
    }

    @Override
    public CategoryDto update(UpdateCategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getId());

        mapper.update(categoryDto, category);

        try {
            repo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Name is already in use", format("Name=%s", category.getName()));
        }
        log.info("{} is updated", category);

        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Category category = getCategory(id);

        if (category.getEvents().isEmpty()) {
            repo.deleteById(id);
        } else {
            throw new ConflictException("Category containing events can not be deleted", "Id=%d");
        }

        log.info("Category with ID {} is removed", id);
    }

    private Category getCategory(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category is not found", format("Id=%d", id)));
    }
}
