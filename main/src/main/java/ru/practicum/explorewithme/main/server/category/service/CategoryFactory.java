package ru.practicum.explorewithme.main.server.category.service;


import ru.practicum.explorewithme.main.server.category.model.Category;

public interface CategoryFactory {

    Category getById(Long id);
}
