package ru.practicum.explorewithme.main.server.event.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.main.server.category.model.Category;
import ru.practicum.explorewithme.main.server.compilation.model.Compilation;
import ru.practicum.explorewithme.main.server.request.model.Request;
import ru.practicum.explorewithme.main.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "requests", "compilations"})
@ToString(exclude = {"requests", "compilations"})
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 2000)
    private String annotation;

    @NonNull
    @Column(nullable = false, length = 120)
    private String title;

    @NonNull
    @Column(nullable = false, length = 7000)
    private String description;

    @NonNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @NonNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User initiator;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    @Builder.Default
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @CreationTimestamp
    private LocalDateTime created;

    private LocalDateTime published;

    @OneToMany(mappedBy = "event",
            orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations = new ArrayList<>();
}