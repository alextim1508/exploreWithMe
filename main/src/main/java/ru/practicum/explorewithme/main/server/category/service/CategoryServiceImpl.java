package ru.practicum.explorewithme.main.server.category.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.UpdateCategoryDto;
import ru.practicum.explorewithme.main.server.category.model.Category;
import ru.practicum.explorewithme.main.server.category.repository.CategoryRepository;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.util.OffsetLimitPageable;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper mapper;
    private final CategoryRepository repo;
    private final MessageSource messageSource;

    @Override
    public CategoryDto create(NewCategoryDto categoryDto) {
        log.debug("Request to add category {} is received", categoryDto);

        Category category = mapper.toEntity(categoryDto);

        Category savedCategory;
        try {
            savedCategory = repo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "category.name_is_duplicate", new Object[]{category.getName()}, null),
                    format("Name=%s", category.getName()));
        }

        log.debug("{} is added to repository", savedCategory);

        return mapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto getById(Long id) {
        log.debug("Category with id {} is requested", id);

        Category category = getCategory(id);

        log.debug("Category with id {} is received from repository", id);

        return mapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.debug("List of categories is requested with the following pagination parameters: from={} and size={}",
                from, size);

        List<Category> categories = repo.findAll(OffsetLimitPageable.of(from, size)).getContent();

        log.debug("List of categories is received from repository with size of {}", categories.size());

        return mapper.toDto(categories);
    }

    @Override
    public CategoryDto update(Long categoryId, UpdateCategoryDto categoryDto) {
        log.debug("Request to update category with ID {} is received", categoryId);

        Category category = getCategory(categoryId);

        mapper.update(categoryDto, category);

        try {
            repo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    messageSource.getMessage(
                            "category.name_is_duplicate", new Object[]{category.getName()}, null),
                    format("Name=%s", category.getName()));
        }

        log.info("{} is updated in repository", category);

        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Request to delete category with ID {} is received", id);

        Category category = getCategory(id);

        if (!category.getEvents().isEmpty()) {
            throw new ConflictException(
                    messageSource.getMessage("category.delete.used_in_events", new Object[]{id}, null),
                    format("categoryId=%d, eventIds=%s", id, category.getEvents().stream()
                            .map(event -> String.valueOf(event.getId()))
                            .collect(Collectors.joining(", "))));
        }

        repo.deleteById(id);

        log.debug("Category with ID {} is deleted from repository", id);
    }

    private Category getCategory(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("category.not_found", new Object[]{id}, null),
                        format("categoryId=%d", id)));
    }
}
