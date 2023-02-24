package ru.practicum.explorewithme.main.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.server.exception.ConflictException;
import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;
import ru.practicum.explorewithme.main.server.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
public class UserServiceIT {

    @Autowired
    UserService userService;

    NewUserDto userDtoIn1;
    NewUserDto userDtoIn2;

    @BeforeEach
    void setUp() {
        userDtoIn1 = NewUserDto.builder()
                .name("user1")
                .email("mail1@gmail.com")
                .build();
        userDtoIn2 = NewUserDto.builder()
                .name("user2")
                .email("mail2@gmail.com")
                .build();
    }

    @Test
    public void shouldSaveUser() {
        List<UserFullDto> users  = userService.get(0, 10, null);
        assertThat(users)
                .isEmpty();

        UserFullDto savedUser = userService.create(userDtoIn1);
        users  = userService.get(0, 10, List.of(savedUser.getId()));
        assertThat(users)
                .hasSize(1).contains(savedUser);
    }

    @Test
    public void shouldThrowExceptionWithDuplicateEmail() {
        userService.create(userDtoIn1);

        userDtoIn2.setEmail(userDtoIn1.getEmail());

        assertThatThrownBy(() -> userService.create(userDtoIn2))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void shouldGetAllUsers() {
        UserFullDto savedUser1 = userService.create(userDtoIn1);
        UserFullDto savedUser2 = userService.create(userDtoIn2);

        assertThat(userService.get(0, 10, null))
                .hasSize(2)
                .containsAll(List.of(savedUser1, savedUser2));
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void shouldDeleteUser() {
        UserFullDto savedUser1 = userService.create(userDtoIn1);
        UserFullDto savedUser2 = userService.create(userDtoIn2);

        userService.delete(savedUser1.getId());

        assertThat(userService.get(0, 10, null))
                .hasSize(1)
                .containsAll(List.of(savedUser2));
    }

}