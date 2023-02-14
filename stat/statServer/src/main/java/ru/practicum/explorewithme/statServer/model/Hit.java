package ru.practicum.explorewithme.statServer.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String app;

    @NonNull
    @Column(nullable = false)
    private String uri;

    @NonNull
    @Column(nullable = false)
    private String ip;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime created;
}
