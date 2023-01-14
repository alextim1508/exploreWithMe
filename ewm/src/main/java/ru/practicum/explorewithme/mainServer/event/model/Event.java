package ru.practicum.explorewithme.mainServer.event.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.mainServer.category.model.Category;
import ru.practicum.explorewithme.mainServer.compilation.model.Compilation;
import ru.practicum.explorewithme.mainServer.request.model.Request;
import ru.practicum.explorewithme.mainServer.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 7000)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User initiator;

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

    @OneToMany(mappedBy = "event",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private Set<Request> requests = new HashSet<>();

    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations = new HashSet<>();

    @Transient
    private long confirmedRequests;

    @Transient
    private long views;
}