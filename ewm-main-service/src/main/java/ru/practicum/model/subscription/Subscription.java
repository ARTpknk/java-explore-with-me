package ru.practicum.model.subscription;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "subscriber_id", nullable = false)
    private Long subscriberId;
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
    @Transient
    private User subscriber;
    @Transient
    private User creator;   //создатель объявлений

    public Subscription() {
    }

    public Subscription(Long id, Long subscriberId, Long creatorId, User subscriber, User creator) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
        this.subscriber = subscriber;
        this.creator = creator;
    }

    public Subscription(Long id, Long subscriberId, Long creatorId) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
    }
}