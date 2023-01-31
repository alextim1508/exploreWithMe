package ru.practicum.explorewithme.mainServer.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.user.model.User;
import ru.practicum.explorewithme.mainServer.user.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class UserFactoryImpl implements UserFactory {

    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found", String.format("Id=%d", id)));
    }
}
