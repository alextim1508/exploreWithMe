package ru.practicum.explorewithme.main.server.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.main.server.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
