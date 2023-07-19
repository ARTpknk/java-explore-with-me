package ru.practicum.service.event;

import ru.practicum.dto.event.EventFilter;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventRequest;

import java.util.List;
import java.util.Set;

public interface EventService {
    Event createEvent(Long userId, NewEvent newEvent);

    List<Event> getEventsOfUser(Long userId, int from, int size);

    Event getEventOfUser(Long userId, Long eventId);

    Event updateEventByAdmin(Long id, UpdateEventRequest updateEventRequest);

    Event getEventById(Long id);

    List<Event> getEventsByIds(Set<Long> ids);

    void addConfirmedRequest(Event event);

    void deleteConfirmedRequest(Event event);

    Event updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventDto);

    List<Event> getAllEventsByAdmin(EventFilter eventFilter);

    List<Event> getAllEventsByPublic(EventFilter eventFilter, String uri, String ip);

    Event getEventByIdByPublicRequest(Long id, String uri, String ip);
}