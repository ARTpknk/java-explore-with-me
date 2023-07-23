package ru.practicum.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.model.event.Event;
import ru.practicum.model.state.State;
import ru.practicum.model.subscription.Subscription;
import ru.practicum.model.user.User;
import ru.practicum.repository.SubscriptionRepository;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository repository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    @Transactional
    public Subscription subscribe(Long subscriberId, Long creatorId) {
        if (Objects.equals(subscriberId, creatorId)) {
            throw new ExploreWithMeConflictException("нельзя подписаться на самого себя");
        }
        if (!isSubscriptionExists(subscriberId, creatorId)) {
            Subscription subscription = Subscription.builder()
                    .subscriberId(subscriberId)
                    .creatorId(creatorId)
                    .build();

            userService.addSubscription(subscriberId);
            userService.addSubscriber(creatorId);
            repository.save(subscription);
            addUsers(subscription);
            return subscription;
        } else {
            throw new ExploreWithMeConflictException("вы уже подписаны");
        }
    }

    @Override
    public void unsubscribe(Long subscriberId, Long creatorId) {
        if (Objects.equals(subscriberId, creatorId)) {
            throw new ExploreWithMeConflictException("нельзя подписаться на самого себя, и отписаться тоже");
        }
        if (isSubscriptionExists(subscriberId, creatorId)) {
            userService.deleteSubscription(subscriberId);
            userService.deleteSubscriber(creatorId);
            Subscription subscription = getSubscriptionByUsers(subscriberId, creatorId);
            repository.deleteById(subscription.getId());
        } else {
            throw new ExploreWithMeConflictException("вы не подписаны");
        }
    }

    @Override
    public Boolean isSubscriptionExists(Long subscriberId, Long creatorId) {
        return repository.findBySubscriberIdAndCreatorId(subscriberId, creatorId) != null;
    }

    @Override
    public Subscription getSubscriptionByUsers(Long subscriberId, Long creatorId) {
        if (repository.findBySubscriberIdAndCreatorId(subscriberId, creatorId) != null) {
            return repository.findBySubscriberIdAndCreatorId(subscriberId, creatorId);
        } else {
            throw new ExploreWithMeConflictException("такой подписки нет");
        }
    }

    @Override
    public List<User> getSubscribes(Long subscriberId, int from, int size) {
        return repository.findAllBySubscriberId(subscriberId, PageRequest.of(from, size))
                .stream()
                .map((this::addUsers))
                .map(Subscription::getCreator)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getSubscribers(Long creatorId, int from, int size) {
        return repository.findAllByCreatorId(creatorId, PageRequest.of(from, size))
                .stream()
                .map((this::addUsers))
                .map(Subscription::getSubscriber)
                .collect(Collectors.toList());
    }

    @Override
    public Subscription addUsers(Subscription subscription) {
        User subscriber = userService.getUserById(subscription.getSubscriberId());
        User creator = userService.getUserById(subscription.getCreatorId());
        subscription.setSubscriber(subscriber);
        subscription.setCreator(creator);
        return subscription;
    }

    @Override
    public List<Event> getSubscribedEvents(Long subscriberId, int from, int size) {
        Long[] creatorsArray = repository.findAllBySubscriberId(subscriberId)
                .stream()
                .map(Subscription::getCreatorId)
                .toArray(Long[]::new);
        String[] states = new String[1];
        states[0] = State.PUBLISHED.toString();
        EventFilter filter = EventFilter.builder()
                .states(states)
                .users(creatorsArray)
                .from(from)
                .size(size)
                .build();
        return eventService.getAllEventsByAdmin(filter);
    }
}