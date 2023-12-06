package ru.practicum.service.subscription;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.location.Location;
import ru.practicum.model.state.State;
import ru.practicum.model.state.StateAction;
import ru.practicum.model.subscription.Subscription;
import ru.practicum.model.user.User;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        properties = "db.name=subscriptionServiceTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubscriptionServiceTest {
    @Autowired
    SubscriptionService service;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @MockBean
    EventService eventService;

    private static final Long id1 = 1L;
    private static final Long id2 = 2L;


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

        NewEvent newEvent = NewEvent.builder().annotation("Футбол").category(1L).description("Футбол в мороз")
                .eventDate("2024-12-31 15:10:05").location(location).paid(true)
                .participantLimit(100).requestModeration(false).title("Футбол").build();
        Event event = new Event(1L, "Футбол", category, 0, LocalDateTime.now(), "Футбол в мороз",
                LocalDateTime.now().plusDays(3), user2, location, true, 100, LocalDateTime.now().plusSeconds(10), false,
                State.PUBLISHED, "Футбол", 1);

        eventService.createEvent(id2, newEvent);

        UpdateEventRequest updateEventRequest = UpdateEventRequest.builder().stateAction(StateAction.PUBLISH_EVENT).build();
        eventService.updateEventByAdmin(1L, updateEventRequest);

        user2.setSubscribers(1);
        user1.setSubscriptions(1);
        Subscription subscription = new Subscription(1L, id1, id2, user1, user2);
        Subscription newSubscription = service.subscribe(id1, id2);
        assertThat(subscription, equalTo(newSubscription));
        assertThat(subscription, equalTo(service.getSubscriptionByUsers(id1, id2)));

        assertThat(true, equalTo(service.isSubscriptionExists(id1, id2)));

        List<User> list = new ArrayList<>();
        list.add(user1);
        assertThat(list, equalTo(service.getSubscribers(id2, 0, 100)));

        list.clear();
        list.add(user2);
        assertThat(list, equalTo(service.getSubscribes(id1, 0, 100)));


        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventService.getAllEventsByAdmin(any()))
                .thenReturn(events);
        assertThat(events, equalTo(service.getSubscribedEvents(id1, 0, 100)));

        service.unsubscribe(id1, id2);
        assertThat(false, equalTo(service.isSubscriptionExists(id1, id2)));
    }
}