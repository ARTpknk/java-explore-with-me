package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.BaseClient;
import ru.practicum.StatsDto;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.state.State;
import ru.practicum.model.state.StateAction;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final BaseClient baseClient;
    private final Clock clock;

    @Value("ewm-stats-service")
    private String app;


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
    public List<Event> getEventsByIds(Set<Long> ids) {
        try {
            return repository.findAllById(ids);
        } catch (Exception e) {
            throw new ExploreWithMeConflictException("Передан несуществующий event");
        }
    }

    @Override
    @Transactional
    public Event updateEventByAdmin(Long id, UpdateEventRequest updateEventAdminRequest) {
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
            event.setPublishedOn(DateFormatter.toLocalDateTime(LocalDateTime.now(clock).format(formatter)));
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
    public Event updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEvent) {
        if (updateEvent.getEventDate() != null) {
            LocalDateTime createdOn = DateFormatter.toLocalDateTime(updateEvent.getEventDate());
            checkEventDate(createdOn);
        }
        Event event = getEventById(eventId);

        if (event.getState() == State.REJECTED || event.getState() == State.PENDING) {
            if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
            if (updateEvent.getStateAction() == StateAction.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
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
    public List<Event> getAllEventsByAdmin(EventFilter eventFilter) {

        EventSpecification specification = new EventSpecification(eventFilter, clock);

        PageRequest pageRequest = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(),
                eventFilter.getSize());

        List<Event> events = repository.findAll(specification, pageRequest).getContent();
        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        return getViews(events, "/events");
    }

    @Override
    public List<Event> getAllEvents(EventFilter eventFilter) {
        EventSpecification specification = new EventSpecification(eventFilter, clock);

        PageRequest pageRequest = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(),
                eventFilter.getSize());
        return repository.findAll(specification, pageRequest).getContent();
    }

    @Override
    public List<Event> getAllEventsByPublic(EventFilter eventFilter, String uri, String ip) {
        Long[] categories = eventFilter.getCategories();
        if (eventFilter.getCategories() != null) {
            for (Long category : categories) {
                if (category < 1) {
                    throw new ExploreWithMeBadRequest("Нет неположительных категорий");
                }
            }
        }
        String[] state = new String[1];
        state[0] = State.PUBLISHED.toString();
        eventFilter.setStates(state);

        if (eventFilter.getRangeStart() == null && eventFilter.getRangeEnd() == null) {
            eventFilter.setRangeStart(LocalDateTime.now(clock));
        }
        EventSpecification specification = new EventSpecification(eventFilter, clock);

        PageRequest pageRequest = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(),
                eventFilter.getSize());

        List<Event> events = repository.findAll(specification, pageRequest).getContent();
        List<String> uris = events.stream().map(event -> uri + "/" + event.getId()).collect(Collectors.toList());
        for (String u : uris) {
            baseClient.postRequest(app, u, ip, LocalDateTime.now(clock));
        }
        if (events.isEmpty()) {
            return Collections.emptyList();
        }
        return getViews(events, uri);
    }

    @Override
    public Event getEventByIdByPublicRequest(Long id, String uri, String ip) {
        Event event = getEventById(id);
        if (event.getState() != State.PUBLISHED) {
            throw new ExploreWithMeNotFoundException("нет такого события");
        }
        baseClient.postRequest(app, uri, ip, LocalDateTime.now(clock));
        return getViewsForOneEvent(event, uri);
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

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now(clock).plusHours(2))) {
            throw new ExploreWithMeBadRequest("Start date must be least 2 hours before");
        }
    }

    private List<Event> getViews(List<Event> events, String uri) {
        if (events.isEmpty()) {
            return events;
        }
        LocalDateTime start = LocalDateTime.now(clock).minusYears(15);
        LocalDateTime end = LocalDateTime.now(clock).plusYears(15);
        //события уже отобраны по времени

        List<String> uris = events.stream().map(event -> uri + "/" + event.getId()).collect(Collectors.toList());
        List<StatsDto> stats = baseClient.getStats(start, end, uris, true);
        if (!stats.isEmpty()) {
            Map<String, Long> mappedStatsByUri = stats.stream()
                    .collect(Collectors.toMap(StatsDto::getUri, StatsDto::getHits));
            events = events.stream()
                    .map(event -> {
                        if (mappedStatsByUri.get(uri + "/" + event.getId()) != null) {
                            return event.withViews(
                                    mappedStatsByUri.get(uri + "/" + event.getId()).intValue());
                        }
                        return event;
                    })
                    .collect(Collectors.toList());
        }
        return events;
    }

    private Event getViewsForOneEvent(Event event, String uri) {
        LocalDateTime start = LocalDateTime.now().minusYears(15);
        LocalDateTime end = LocalDateTime.now().plusYears(15);
        //события уже отобраны по времени

        List<String> uris = new ArrayList<>();
        uris.add(uri);

        List<StatsDto> stats = baseClient.getStats(start, end, uris, true);

        if (!stats.isEmpty()) {
            return event.withViews(Math.toIntExact(stats.get(0).getHits()));
        }
        return event;
    }
}