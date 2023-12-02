package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserServiceTest {
    @Autowired
    UserService service;
    private static User user1;
    private static User user2;
    private static User user3;

    private static final Long id1 = 1L;
    private static final Long id2 = 2L;
    private static final Long id3 = 3L;


    @BeforeAll
    public static void makeUserForTests() {
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
    void test() {
        User newUser = service.create(user1);
        assertThat(newUser, equalTo(user1));

        User getUser = service.getUserById(id1);
        assertThat(getUser, equalTo(user1));

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);
        service.create(user2);
        service.create(user3);
        List<User>getUsers = service.getUsersByIds(ids, 0, 100);
        assertThat(getUsers, equalTo(users));

        users.add(user3);
        List<User>getAllUsers = service.getUsers( 0, 100);
        assertThat(getAllUsers, equalTo(users));

        service.deleteUserById(id3);
        users.remove(user3);
        List<User>getAllUsers2 = service.getUsers( 0, 100);
        assertThat(getAllUsers2, equalTo(users));

        service.addSubscriber(id1);
        service.addSubscription(id2);
        user1.setSubscribers(1);
        user2.setSubscriptions(1);
        assertThat(service.getUserById(id1), equalTo(user1));
        assertThat(service.getUserById(id2), equalTo(user2));

        service.deleteSubscriber(id1);
        service.deleteSubscription(id2);
        user1.setSubscribers(0);
        user2.setSubscriptions(0);
        assertThat(service.getUserById(id1), equalTo(user1));
        assertThat(service.getUserById(id2), equalTo(user2));
    }
}