package ru.practicum.explorewithme.main.server.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.explorewithme.main.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.server.category.service.CategoryService;
import ru.practicum.explorewithme.main.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.server.event.dto.LocationDto;
import ru.practicum.explorewithme.main.server.event.dto.NewEventDto;
import ru.practicum.explorewithme.main.server.event.repository.EventFilter;
import ru.practicum.explorewithme.main.server.event.service.EventService;
import ru.practicum.explorewithme.main.server.user.dto.NewUserDto;
import ru.practicum.explorewithme.main.server.user.dto.UserFullDto;
import ru.practicum.explorewithme.main.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class EventServiceIT {

    @Autowired
    EventService eventService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    NewEventDto eventDto;

    NewUserDto initiator;

    NewCategoryDto categoryDto;

    LocationDto locationSpbDto = new LocationDto(59.9342802, 30.3350986);

    @BeforeEach
    void setUp() {
        initiator = NewUserDto.builder()
                .name("initiator")
                .email("initiator@mail.com")
                .build();

        categoryDto = new NewCategoryDto("name");

        eventDto = NewEventDto.builder()
                .annotation("annotation")
                .title("title")
                .description("description")
                .categoryId(1L)
                .eventDate(LocalDateTime.now().plusDays(1))
                .location(locationSpbDto)
                .build();
    }


    @Test
    public void shouldSaveEvent() {
        UserFullDto savedInitiator = userService.create(initiator);

        CategoryDto savedCategory = categoryService.create(categoryDto);

        EventFilter filter = EventFilter.builder()
                        .from(0)
                        .size(10)
                        .build();

        List<EventFullDto> events = eventService.getAll(filter);
        assertThat(events)
                .isEmpty();

        eventDto.setCategoryId(savedCategory.getId());
        EventFullDto savedEvent = eventService.create(savedInitiator.getId(), eventDto);

        events = eventService.getAll(filter);
        assertThat(events)
                .hasSize(1).contains(savedEvent);
    }




}
