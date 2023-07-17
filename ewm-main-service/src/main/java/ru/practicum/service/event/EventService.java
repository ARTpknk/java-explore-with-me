package ru.practicum.service.event;

import ru.practicum.dto.event.EventFilter;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventAdminRequest;
import ru.practicum.model.event.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    Event createEvent(Long userId, NewEvent newEvent);

    List<Event> getEventsOfUser(Long userId, int from, int size);

    Event getEventOfUser(Long userId, Long eventId);

    Event updateEventByAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    Event getEventById(Long id);

    void addConfirmedRequest(Event event);

    void deleteConfirmedRequest(Event event);

    List<Event> getEventsByCategory(Long categoryId);

    Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventDto);

    List<Event> getAllEventsByAdmin(EventFilter eventFilter);

    List<Event> getAllEventsByPublic(EventFilter eventFilter, String uri, String ip);
}