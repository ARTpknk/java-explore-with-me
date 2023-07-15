package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.NewEvent;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;


    @Override
    @Transactional
    public Event createEvent(Long userId, NewEvent newEvent) {
        LocalDateTime createdOn = DateFormatter.toLocalDateTime(newEvent.getEventDate());
        checkEventDate(createdOn);
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(newEvent.getCategory());
        return repository.save(EventMapper.fromNewEventToEvent(newEvent, category, user, createdOn));
    }

    @Override
    public List<Event> getEventsOfUser(Long userId, int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }

    @Override
    public Event getEventOfUser(Long userId, Long eventId) {
        if (repository.findById(eventId).isPresent()) {
            Event event = repository.findById(eventId).get();
            if (Objects.equals(event.getInitiator().getId(), userId)) {
                return event;
            } else {
                throw new ExploreWithMeBadRequest("Event with Id: " + eventId +
                        " has another initiator, not initiator with Id = " + userId);
            }
        } else {
            throw new ExploreWithMeNotFoundException("Event with Id: " + eventId + " not found");
        }
    }


    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ExploreWithMeBadRequest("Start date must be least 2 hours before");
        }
    }
}