package ru.practicum.explorewithme.mainServer.event.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.mainServer.event.model.Event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EventSpecification implements Specification<Event> {

    private final EventFilter filter;

    @Override
    public Predicate toPredicate(Root<Event> event, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getText() != null) {
            predicates.add(cb.or(
                    cb.like(cb.upper(event.get("annotation")), "%" + filter.getText().toUpperCase() + "%"),
                    cb.like(cb.upper(event.get("description")), "%" + filter.getText().toUpperCase() + "%"))
            );
        }
        if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
            predicates.add(cb.in(
                    event.get("category").get("id")).value(filter.getCategoryIds()));
        }
        if (filter.getPaid() != null) {
            predicates.add(cb.equal(
                    event.get("paid"), filter.getPaid()));
        }

        if (filter.getUserIds() != null && !filter.getUserIds().isEmpty()) {
            predicates.add(cb.in(event.get("initiator").get("id")).value(filter.getUserIds()));
        }

        if (filter.getStates() != null && !filter.getStates().isEmpty()) {
            predicates.add(cb.in(event.get("state")).value(filter.getStates()));
        }


        if (filter.getRangeStart() != null) {
            predicates.add(cb.greaterThan(event.get("eventDate"), filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            predicates.add(cb.lessThan(event.get("eventDate"), filter.getRangeEnd()));
        }

//        EntityGraph<Event> entityGraph = em.createEntityGraph(Event.class);
//        entityGraph.addAttributeNodes("initiator");
//        entityGraph.addAttributeNodes("category");
//        entityGraph.addSubgraph("requests").addAttributeNodes("requester");


        return query
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .getRestriction();

    }
}
