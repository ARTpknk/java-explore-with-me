package ru.practicum.service.user;

import ru.practicum.model.user.User;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getUsersByIds(List<Long> ids, int from, int size);

    List<User> getUsers(int from, int size);

    User getUserById(Long id);

    void deleteUserById(Long id);

    void addSubscription(Long id);

    void addSubscriber(Long id);

    void deleteSubscription(Long id);

    void deleteSubscriber(Long id);
}