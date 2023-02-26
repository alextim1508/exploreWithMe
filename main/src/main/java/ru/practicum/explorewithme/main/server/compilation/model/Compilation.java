package ru.practicum.explorewithme.main.server.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.main.server.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "events"})
@ToString(exclude = {"events"})
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 1024, unique = true)
    private String title;

    private Boolean pinned = false;

    @ManyToMany
    @JoinTable(name = "events_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();
}
