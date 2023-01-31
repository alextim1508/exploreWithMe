package ru.practicum.explorewithme.mainServer.event.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.mainServer.category.model.Category;
import ru.practicum.explorewithme.mainServer.compilation.model.Compilation;
import ru.practicum.explorewithme.mainServer.request.model.Request;
import ru.practicum.explorewithme.mainServer.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @CreationTimestamp
    private LocalDateTime created;

    private LocalDateTime published;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "event",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations = new ArrayList<>();
}