package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getUsers(List<Long> ids, int from, int size) {
        return repository.findAllById(ids, PageRequest.of(from, size)).toList();
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
}