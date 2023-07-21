package ru.practicum.service.subscription;

import ru.practicum.model.event.Event;
import ru.practicum.model.subscription.Subscription;
import ru.practicum.model.user.User;

import java.util.List;

public interface SubscriptionService {
    Subscription subscribe(Long subscriberId, Long creatorId);

    void unsubscribe(Long subscriberId, Long creatorId);

    Boolean isSubscriptionExists(Long subscriberId, Long creatorId);

    Subscription getSubscriptionByUsers(Long subscriberId, Long creatorId);

    List<User> getSubscribes(Long subscriberId, int from, int size);

    List<User> getSubscribers(Long creatorId, int from, int size);

    Subscription addUsers(Subscription subscription);

    List<Event> getSubscribedEvents(Long subscriberId, int from, int size);
}
