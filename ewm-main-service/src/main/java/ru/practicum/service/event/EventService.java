package ru.practicum.service.event;

import ru.practicum.model.Event;
import ru.practicum.model.NewEvent;

import java.util.List;

public interface EventService {
    Event createEvent(Long userId, NewEvent newEvent);
    List<Event> getEventsOfUser(Long userId, int from, int size);
    Event getEventOfUser(Long userId, Long eventId);
}