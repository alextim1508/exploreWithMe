package ru.practicum.explorewithme.mainServer.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.mainServer.exception.ConflictException;
import ru.practicum.explorewithme.mainServer.exception.NotFoundException;
import ru.practicum.explorewithme.mainServer.user.dto.NewUserDto;
import ru.practicum.explorewithme.mainServer.user.dto.UserFullDto;
import ru.practicum.explorewithme.mainServer.user.model.User;
import ru.practicum.explorewithme.mainServer.user.repository.UserRepository;
import ru.practicum.explorewithme.mainServer.util.OffsetLimitPageable;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserFullDto create(NewUserDto userDto) {
        User user = mapper.toEntity(userDto);

        User savedUser;
        try {
            savedUser = repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email is already in use", format("Email=%s", user.getEmail()));
        }
        log.info("{} is saved", savedUser);

        return mapper.toFullDto(savedUser);
    }

    @Override
    public List<UserFullDto> get(Integer from, Integer size, @Nullable List<Long> ids) {
        Pageable pageable = OffsetLimitPageable.of(from, size);

        List<User> users;
        if (ids == null) {
            users = repo.findAll(pageable).getContent();
        } else {
            users = repo.findAllByIdIn(ids, pageable);
        }
        log.info("{} users are found", users.size());

        return mapper.toFullDto(users);
    }

    @Override
    public void delete(Long id) {
        try {
            repo.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("User is not found", format("Id=%d", id));
        }
        log.info("User with ID {} is removed", id);
    }
}
