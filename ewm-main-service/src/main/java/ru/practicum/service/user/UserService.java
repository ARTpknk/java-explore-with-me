package ru.practicum.service.user;

import ru.practicum.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getUsers(List<Long> ids, int from, int size);

    User getUserById(Long id);

    void deleteUserById(Long id);
}