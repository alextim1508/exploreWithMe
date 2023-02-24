package ru.practicum.explorewithme.main.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.exception.NotFoundException;
import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;
import ru.practicum.explorewithme.main.server.user.model.User;
import ru.practicum.explorewithme.main.server.user.repository.UserRepository;
import ru.practicum.explorewithme.main.server.util.OffsetLimitPageable;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;
    private final MessageSource messageSource;

    @Override
    public UserFullDto create(NewUserDto userDto) {
        log.debug("Request to add {} is received", userDto);

        User user = mapper.toEntity(userDto);

        User savedUser;
        try {
            savedUser = repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    messageSource.getMessage("user.email.duplicate", null, null),
                    format("Email=%s", user.getEmail()));
        }

        log.debug("User with ID {} is added to repository", savedUser);

        return mapper.toFullDto(savedUser);
    }

    @Override
    public List<UserFullDto> get(Integer from, Integer size, @Nullable List<Long> ids) {
        log.debug("List of users is requested with the following pagination parameters: from={} and size={}",
                from, size);

        Pageable pageable = OffsetLimitPageable.of(from, size);

        List<User> users;
        if (ids == null) {
            users = repo.findAll(pageable).getContent();
        } else {
            log.debug("ID criteria={}", ids);
            users = repo.findAllByIdIn(ids, pageable);
        }

        log.debug("List of users is received from repository with size of {}", users.size());

        return mapper.toFullDto(users);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Request to delete user with ID {} is received", id);

        User user = getUser(id);

        if (!user.getEvents().isEmpty()) {
            throw new ConflictException(
                    messageSource.getMessage("user.delete.used_in_events", new Object[]{id}, null),
                    format("userId=%d, eventIds=%s", id, user.getEvents().stream().map(event ->
                            String.valueOf(event.getId())).collect(Collectors.joining(", "))));

        }

        if (!user.getRequests().isEmpty()) {
            throw new ConflictException(
                    messageSource.getMessage("user.delete.used_in_requests", new Object[]{id}, null),
                    format("userId=%d, requestIds=%s", id, user.getRequests().stream().map(event ->
                            String.valueOf(event.getId())).collect(Collectors.joining(", "))));
        }

        repo.deleteById(id);

        log.info("User with ID {} is deleted from repository", id);
    }

    User getUser(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        messageSource.getMessage("user.not_found", new Object[]{id}, null),
                        format("userId=%d", id)));
    }
}
