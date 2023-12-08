package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.location.Location;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.state.StateAction;
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

@Transactional
@SpringBootTest(
        properties = "db.name=requestServiceTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    @Autowired
    RequestService service;
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    private static final Long id1 = 1L;
    private static final Long id2 = 2L;
    String eventDate1 = "2024-12-31 15:10:05";
    String eventDate2 = "2024-12-29 15:10:05";

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

        eventService.createEvent(id2, newEvent1);
        eventService.createEvent(id2, newEvent2);

        ParticipationRequest participationRequest1 = ParticipationRequest.builder()
                .id(1L)
                .event(eventService.getEventById(1L))
                .requester(user1)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();

        UpdateEventRequest updateEventRequest = UpdateEventRequest.builder().
                stateAction(StateAction.PUBLISH_EVENT)
                .build();
        eventService.updateEventByAdmin(1L, updateEventRequest);
        eventService.updateEventByAdmin(2L, updateEventRequest);

        ParticipationRequest request1 = service.createRequest(id1, 1L);
        assertThat(participationRequest1.getId(), equalTo(request1.getId()));
        assertThat(participationRequest1.getEvent(), equalTo(request1.getEvent()));
        assertThat(participationRequest1.getRequester(), equalTo(request1.getRequester()));
        assertThat(participationRequest1.getStatus(), equalTo(request1.getStatus()));

        ParticipationRequest request2 = service.createRequest(id1, 2L);
        List<ParticipationRequest> requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);
        assertThat(requests, equalTo(service.getRequestsOfUser(id1)));

        requests.remove(request2);
        assertThat(requests, equalTo(service.getRequestsOfEvent(id2, 1L)));

        assertThat(request2, equalTo(service.getRequestById(2L)));

        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest = EventRequestStatusUpdateRequest.builder()
                .requestIds(ids)
                .status(RequestStatus.CONFIRMED)
                .build();
        service.updateRequests(id2, 1L, eventRequestStatusUpdateRequest);
        request1.setStatus(RequestStatus.CONFIRMED);
        assertThat(request1, equalTo(service.getRequestById(1L)));

        service.cancelRequest(id1, 2L);
        request2.setStatus(RequestStatus.CANCELED);
        assertThat(request2, equalTo(service.getRequestById(2L)));
    }
}