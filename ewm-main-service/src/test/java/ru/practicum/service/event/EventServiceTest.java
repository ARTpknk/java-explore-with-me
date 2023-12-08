package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.location.Location;
import ru.practicum.model.state.State;
import ru.practicum.model.state.StateAction;
import ru.practicum.model.user.User;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=eventServiceTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceTest {
    @Autowired
    EventService service;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    private static final Long id1 = 1L;
    private static final Long id2 = 2L;
    String eventDate1 = "2024-12-31 15:10:05";
    String eventDate2 = "2024-12-29 15:10:05";
    String eventDate3 = "2025-12-29 15:10:05";

    @Test
    void test() {
        User user1 = User.builder()
                .id(id1)
                .name("user")
                .email("user@email.ru")
                .build();
        userService.create(user1);
        User user2 = User.builder()
                .id(id2)
                .name("user2")
                .email("user2@email.ru")
                .build();
        userService.create(user2);

        Category category = new Category(1L, "Спорт");
        categoryService.createCategory(category);

        Location location = new Location(10.1F, 10.1F);
        Event event1 = new Event(1L, "Футбол", category, 0, LocalDateTime.now(),
                "Футбол в мороз", DateFormatter.toLocalDateTime(eventDate1), user2, location, true,
                100, LocalDateTime.now().plusSeconds(3), true, State.PENDING,
                "Футбол", 0);

        NewEvent newEvent1 = NewEvent.builder()
                .annotation("Футбол")
                .category(1L)
                .description("Футбол в мороз")
                .eventDate(eventDate1)
                .location(location)
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .title("Футбол")
                .build();

        NewEvent newEvent2 = NewEvent.builder()
                .annotation("Баскетбол")
                .category(1L)
                .description("Баскетбол в тепле")
                .eventDate(eventDate2)
                .location(location).
                paid(false)
                .participantLimit(100)
                .requestModeration(false)
                .title("Баскетбол")
                .build();

        NewEvent newEvent3 = NewEvent.builder()
                .annotation("Гольф")
                .category(1L)
                .description("Спорт для богатых")
                .eventDate(eventDate3)
                .location(location)
                .paid(true)
                .participantLimit(3)
                .requestModeration(true)
                .title("Гольф")
                .build();

        Event review1Event1 = service.createEvent(id2, newEvent1);

        assertThat(event1.getId(), equalTo(review1Event1.getId()));
        assertThat(event1.getAnnotation(), equalTo(review1Event1.getAnnotation()));
        assertThat(event1.getCategory(), equalTo(review1Event1.getCategory()));
        assertThat(event1.getConfirmedRequests(), equalTo(review1Event1.getConfirmedRequests()));
        assertThat(event1.getDescription(), equalTo(review1Event1.getDescription()));
        assertThat(event1.getEventDate(), equalTo(review1Event1.getEventDate()));
        assertThat(event1.getRequestModeration(), equalTo(review1Event1.getRequestModeration()));
        assertThat(event1.getState(), equalTo(review1Event1.getState()));
        assertThat(event1.getTitle(), equalTo(review1Event1.getTitle()));
        assertThat(event1.getViews(), equalTo(review1Event1.getViews()));

        Event review2Event1 = service.getEventOfUser(id2, 1L);
        assertThat(review1Event1, equalTo(review2Event1));

        Event review3Event1 = service.getEventById(1L);
        assertThat(review1Event1, equalTo(review3Event1));

        Event event2 = service.createEvent(id2, newEvent2);
        Event event3 = service.createEvent(id1, newEvent3);

        List<Event> events = new ArrayList<>();
        events.add(review1Event1);
        events.add(event2);
        assertThat(events, equalTo(service.getEventsOfUser(id2, 0, 100)));

        events.add(event3);
        EventFilter filter = EventFilter.builder().size(100).from(0).build();

        assertThat(events, equalTo(service.getAllEvents(filter)));

        events.remove(event2);
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(3L);
        assertThat(events, equalTo(service.getEventsByIds(ids)));

        UpdateEventRequest updateEventRequest = UpdateEventRequest.builder()
                .stateAction(StateAction.PUBLISH_EVENT)
                .build();
        UpdateEventRequest updateEventRequest2 = UpdateEventRequest.builder()
                .paid(false)
                .build();

        service.updateEventByAdmin(1L, updateEventRequest);
        assertThat(service.getEventById(1L).getState(), equalTo(State.PUBLISHED));

        service.updateEventByUser(id1, 3L, updateEventRequest2);
        assertThat(service.getEventById(3L).getPaid(), equalTo(false));

        service.addConfirmedRequest(event3);
        assertThat(1, equalTo(service.getEventById(3L).getConfirmedRequests()));

        service.deleteConfirmedRequest(event3);
        assertThat(0, equalTo(service.getEventById(3L).getConfirmedRequests()));

    }
}