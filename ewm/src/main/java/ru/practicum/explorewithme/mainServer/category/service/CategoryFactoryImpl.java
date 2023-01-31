package ru.practicum.explorewithme.mainServer.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.mainServer.category.model.Category;
import ru.practicum.explorewithme.mainServer.category.repository.CategoryRepository;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;

@RequiredArgsConstructor
@Component
public class CategoryFactoryImpl implements CategoryFactory {

    private final CategoryRepository userRepository;

    @Override
    public Category getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category is not found", String.format("Id=%d", id)));
    }
}
