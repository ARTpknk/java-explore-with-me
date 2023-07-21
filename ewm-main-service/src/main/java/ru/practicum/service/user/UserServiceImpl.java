package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(User user) {
        if (repository.findByEmail(user.getEmail()) != null) {
            throw new ExploreWithMeConflictException((String.format("User with email %s already exists",
                    user.getEmail())));
        }
        return repository.save(user);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids, int from, int size) {
        return repository.findAllById(ids, PageRequest.of(from, size)).toList();
    }

    @Override
    public List<User> getUsers(int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }


    @Override
    public User getUserById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new ExploreWithMeNotFoundException("User with Id: " + id + " not found");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void addSubscription(Long id) {
        User user = getUserById(id);
        user.setSubscriptions(user.getSubscriptions() + 1);
        repository.save(user);
    }

    @Override
    public void addSubscriber(Long id) {
        User user = getUserById(id);
        user.setSubscribers(user.getSubscribers() + 1);
        repository.save(user);
    }

    @Override
    public void deleteSubscription(Long id) {
        User user = getUserById(id);
        int subscriptions = user.getSubscriptions();
        if (subscriptions > 0) {
            user.setSubscriptions(user.getSubscriptions() - 1);
            repository.save(user);
        }
    }

    @Override
    public void deleteSubscriber(Long id) {
        User user = getUserById(id);
        int subscribers = user.getSubscribers();
        if (subscribers > 0) {
            user.setSubscribers(user.getSubscribers() - 1);
            repository.save(user);
        }
    }
}