package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.NewCompilation;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.NewEvent;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=compilationServiceTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationServiceTest {
    @Autowired
    CompilationService service;
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

        eventService.createEvent(id2, newEvent1);
        eventService.createEvent(id2, newEvent2);
        eventService.createEvent(id1, newEvent3);

        Event event1 = eventService.getEventById(1L);
        Event event2 = eventService.getEventById(2L);
        Event event3 = eventService.getEventById(3L);

        Set<Long> eventsIds = new HashSet<>();
        eventsIds.add(1L);
        eventsIds.add(2L);

        Set<Event> events = new HashSet<>();
        events.add(event1);
        events.add(event2);

        NewCompilation newCompilation1 = NewCompilation
                .builder()
                .id(1L)
                .pinned(false)
                .title("Спорт")
                .events(eventsIds)
                .build();

        Compilation compilation1 = Compilation.builder()
                .id(1L)
                .events(events)
                .title("Спорт")
                .pinned(false)
                .build();

        Compilation systemCompilation1 = service.createCompilation(newCompilation1);
        assertThat(compilation1, equalTo(systemCompilation1));

        assertThat(compilation1, equalTo(service.getCompilationById(1L)));

        eventsIds.add(3L);
        NewCompilation updateCompilation1 = NewCompilation
                .builder()
                .id(1L)
                .pinned(false)
                .title("Спорт")
                .events(eventsIds)
                .build();
        events.add(event3);
        compilation1.setEvents(events);
        service.updateCompilation(1L, updateCompilation1);
        assertThat(compilation1, equalTo(service.getCompilationById(1L)));

        List<Compilation> compilations = new ArrayList<>();
        compilations.add(compilation1);
        assertThat(compilations, equalTo(service.getCompilations(0, 100, null)));

        service.deleteCompilationById(1L);
        assertThrows(
                ExploreWithMeNotFoundException.class,
                () -> service.getCompilationById(1L));
    }
}