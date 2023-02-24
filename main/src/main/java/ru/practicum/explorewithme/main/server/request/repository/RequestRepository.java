package ru.practicum.explorewithme.main.server.request.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.server.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);
}
