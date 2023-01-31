package ru.practicum.explorewithme.mainServer.category.service;

import ru.practicum.explorewithme.mainServer.category.model.Category;

public interface CategoryFactory {

    Category getById(Long id);
}
