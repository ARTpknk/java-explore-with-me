package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.model.event.Event;
import ru.practicum.model.state.State;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EventSpecification implements Specification<Event> {
    private final EventFilter eventFilter;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Order order;

        List<Predicate> predicates = new ArrayList<>();

        if (eventFilter.getUsers() != null) {
            predicates.add(root.get("initiator").in(List.of(eventFilter.getUsers())));
        }
        if (eventFilter.getStates() != null) {
            predicates.add(root.get("state")
                    .in(Arrays.stream(eventFilter.getStates()).map(State::valueOf).collect(Collectors.toList())));
        }
        if (eventFilter.getCategories() != null) {
            predicates.add(root.get("category").in(Arrays.asList(eventFilter.getCategories())));
        }
        if (eventFilter.getRangeStart() != null && eventFilter.getRangeEnd() != null) {
            predicates.add(
                    builder.between(
                            root.get("eventDate"),
                            eventFilter.getRangeStart(),
                            eventFilter.getRangeEnd()));
        } else if (eventFilter.getRangeStart() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(
                            root.get("eventDate"),
                            eventFilter.getRangeStart()));
        } else if (eventFilter.getRangeEnd() != null) {
            predicates.add(
                    builder.between(
                            root.get("eventDate"), LocalDateTime.now(),
                            eventFilter.getRangeEnd()));
        } else {
            predicates.add(
                    builder.greaterThanOrEqualTo(
                            root.get("eventDate"),
                            LocalDateTime.now()));
        }
        if (eventFilter.getText() != null) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("annotation")),
                            "%" + eventFilter.getText().toLowerCase() + "%"));
        }
        if (eventFilter.getPaid() != null) {
            predicates.add(
                    builder.equal(root.get("paid"), eventFilter.getPaid()));
        }
        if (eventFilter.getSort() != null) {
            if (eventFilter.getSort().equals("VIEWS")) {
                order = builder.desc(root.get("views"));
            } else {
                order = builder.desc(root.get("eventDate"));
            }
            query.orderBy(order);
        }
        if (eventFilter.getOnlyAvailable()) {
            predicates.add(
                    builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}