package ru.practicum.explorewithme.main.server.request.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "requests",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"requester_id", "event_id"})})
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @CreationTimestamp
    private LocalDateTime created;
}