package ru.practicum.model.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}