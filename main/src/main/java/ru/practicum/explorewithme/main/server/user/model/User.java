package ru.practicum.explorewithme.main.server.user.model;

import lombok.*;
import ru.practicum.explorewithme.main.server.event.model.Event;
import ru.practicum.explorewithme.main.server.request.model.Request;
import ru.practicum.explorewithme.main.server.user.repository.AuditUserListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "events", "requests"})
@ToString(exclude = {"events", "requests"})
@Entity
@EntityListeners(AuditUserListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 255)
    private String name;

    @NonNull
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "initiator")
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "requester")
    private List<Request> requests = new ArrayList<>();
}