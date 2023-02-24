package ru.practicum.explorewithme.main.server.category.model;

import lombok.*;
import ru.practicum.explorewithme.main.server.event.model.Event;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"events"})
@ToString(exclude = {"events"})
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Event> events = new ArrayList<>();
}
