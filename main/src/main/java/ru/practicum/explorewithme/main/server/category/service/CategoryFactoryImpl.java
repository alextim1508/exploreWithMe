package ru.practicum.explorewithme.main.server.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.server.category.model.Category;
import ru.practicum.explorewithme.main.server.category.repository.CategoryRepository;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class CategoryFactoryImpl implements CategoryFactory {

    private final CategoryRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public Category getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("category.not_found", new Object[]{id}, null),
                        format("categoryId=%d", id)));
    }
}
