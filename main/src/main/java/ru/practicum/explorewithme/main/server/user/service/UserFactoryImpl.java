package ru.practicum.explorewithme.main.server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.user.model.User;
import ru.practicum.explorewithme.main.server.user.repository.UserRepository;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {

    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public void checkExisting(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(
                    messageSource.getMessage("user.not_found", new Object[]{id}, null),
                    format("userId=%d", id));
        }
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("user.not_found", new Object[]{id}, null),
                        format("userId=%d", id)));
    }
}
