package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.subscription.Subscription;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);

    List<Subscription> findAllBySubscriberId(Long subscriberId);

    List<Subscription> findAllBySubscriberId(Long subscriberId, Pageable pageable);

    List<Subscription> findAllByCreatorId(Long creatorId, Pageable pageable);
}