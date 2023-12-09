package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=userServiceImplTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTest {
    @Autowired
    UserServiceImpl service;
    private static User user1;
    private static User user2;
    private static User user3;
    private final Long id1 = 1L;
    private static final Long id2 = 2L;
    private static final Long id3 = 3L;
    private final Long wrongId = 99L;

    @BeforeEach
    public void makeUserForTests() {
        user1 = User.builder()
                .id(id1)
                .name("user")
                .email("user@email.ru")
                .build();
        user2 = User.builder()
                .id(id2)
                .name("user2")
                .email("user2@email.ru")
                .build();
        user3 = User.builder()
                .id(id3)
                .name("user3")
                .email("user3@email.ru")
                .build();
    }

    @Test
    void create() {
        User newUser = service.create(user1);
        assertThat(newUser, equalTo(user1));
    }

    @Test
    void createWithSameEmail() {
        service.create(user1);
        service.create(user2);
        User userSameEmail = User.builder()
                .id(id2)
                .name("user2")
                .email("user@email.ru")
                .build();
        assertThrows(
                ExploreWithMeConflictException.class,
                () -> service.create(userSameEmail));
    }

    @Test
    void getUsers() {
        List<User> users = new ArrayList<>();
        List<User> testList = service.getUsers(0, 100);
        assertThat(users, equalTo(testList));
        users.add(user1);
        users.add(user2);
        service.create(user1);
        service.create(user2);
        List<User> testList2 = service.getUsers(0, 100);
        assertThat(users, equalTo(testList2));
    }

    @Test
    void getUsersByIds() {
        List<Long> ids = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<User> testList = service.getUsersByIds(ids, 0, 100);
        assertThat(users, equalTo(testList));
        users.add(user1);
        users.add(user2);
        service.create(user1);
        service.create(user2);
        service.create(user3);
        ids.add(1L);
        ids.add(2L);
        List<User> testList2 = service.getUsersByIds(ids, 0, 100);
        assertThat(users, equalTo(testList2));
    }

    @Test
    void getUserById() {
        service.create(user1);
        User newUser = service.getUserById(id1);
        assertThat(newUser, equalTo(user1));

        assertThrows(
                ExploreWithMeNotFoundException.class,
                () -> service.getUserById(wrongId));
    }

    @Test
    void deleteUserById() {
        service.create(user1);
        service.deleteUserById(id1);
        assertThrows(
                ExploreWithMeNotFoundException.class,
                () -> service.getUserById(id1));
    }

    @Test
    void subscribe() {
        service.create(user1);
        service.create(user2);
        service.addSubscriber(id1);
        service.addSubscription(id2);
        user1.setSubscribers(1);
        user2.setSubscriptions(1);
        assertThat(service.getUserById(id1), equalTo(user1));
        assertThat(service.getUserById(id2), equalTo(user2));
    }

    @Test
    void ubsubscribe() {
        service.create(user1);
        service.create(user2);
        service.addSubscriber(id1);
        service.addSubscription(id2);
        service.deleteSubscriber(id1);
        service.deleteSubscription(id2);
        assertThat(service.getUserById(id1), equalTo(user1));
        assertThat(service.getUserById(id2), equalTo(user2));
    }
}