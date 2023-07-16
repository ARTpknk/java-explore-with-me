package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventAdminRequest;
import ru.practicum.model.event.UpdateEventUserRequest;
import ru.practicum.model.state.State;
import ru.practicum.model.state.StateAction;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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
    @Transactional
    public List<Event> getEventsOfUser(Long userId, int from, int size) {
        return repository.findAllByInitiatorId(userId, PageRequest.of(from, size)).toList();
    }

    @Override
    @Transactional
    public Event getEventOfUser(Long userId, Long eventId) {
        if (repository.findById(eventId).isPresent()) {
            Event event = repository.findById(eventId).get();
            if (event.getInitiator().getId().equals(userId)) {
                return event;
            } else {
                throw new ExploreWithMeBadRequest("Event with Id: " + eventId +
                        " has another initiator, not initiator with Id = " + userId);
            }
        } else {
            throw new ExploreWithMeNotFoundException("Event with Id: " + eventId + " not found");
        }
    }

    @Override
    @Transactional
    public Event getEventById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new ExploreWithMeNotFoundException("Event with Id: " + id + " not found");
        }
    }

    @Override
    @Transactional
    public Event updateEventByAdmin(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime createdOn = DateFormatter.toLocalDateTime(updateEventAdminRequest.getEventDate());
            checkEventDate(createdOn);
        }
        Event event = getEventById(id);
        if (event.getState() == State.PUBLISHED) {
            if (updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT ||
                    updateEventAdminRequest.getStateAction() == StateAction.REJECT_EVENT) {
                throw new ExploreWithMeConflictException(
                        String.format("Event with id: %d is already published", event.getId()));
            }
        } else if (event.getState() == State.REJECTED && updateEventAdminRequest.getStateAction() != null) {
            throw new ExploreWithMeConflictException(
                    String.format("Event with id: %d is already canceled", event.getId()));
        }
        if (updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(DateFormatter.toLocalDateTime(LocalDateTime.now().format(formatter)));
        }
        if (updateEventAdminRequest.getStateAction() == StateAction.REJECT_EVENT) {
            event.setState(State.REJECTED);
        }
        if (updateEventAdminRequest.getCategory() != null) {
            if (!updateEventAdminRequest.getCategory().equals(event.getCategory().getId())) {
                return repository.save(EventMapper.updatingEventByAdmin(event,
                        categoryService.getCategoryById(updateEventAdminRequest.getCategory()),
                        updateEventAdminRequest));
            }
        }
        return repository.save(EventMapper.updatingEventByAdmin(event, event.getCategory(), updateEventAdminRequest));
    }

    @Override
    public Event updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getEventDate() != null) {
            LocalDateTime createdOn = DateFormatter.toLocalDateTime(updateEvent.getEventDate());
            checkEventDate(createdOn);
        }
        Event event = getEventById(eventId);

        if (event.getState() == State.CANCELED || event.getState() == State.PENDING) {
            if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
            if (updateEvent.getCategory() != null) {
                if (!updateEvent.getCategory().equals(event.getCategory().getId())) {
                    return repository.save(EventMapper.updatingEventByUser(event,
                            categoryService.getCategoryById(updateEvent.getCategory()),
                            updateEvent));
                }
            }
            return repository.save(EventMapper.updatingEventByUser(event,
                    event.getCategory(), updateEvent));
        } else {
            throw new ExploreWithMeConflictException("Event cannot be changed");
        }
    }

    @Override
    public void addConfirmedRequest(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        repository.save(event);
    }

    @Override
    public void deleteConfirmedRequest(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        repository.save(event);
    }

    @Override
    public List<Event> getEventsByCategory(Long categoryId) {
        return repository.findAllByCategoryId(categoryId);
    }


    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ExploreWithMeConflictException("Start date must be least 2 hours before");
        }
    }
}