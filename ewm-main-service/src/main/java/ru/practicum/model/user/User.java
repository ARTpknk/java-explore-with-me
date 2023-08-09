package ru.practicum.model.user;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "user_email", length = 512, nullable = false, unique = true)
    private String email;
    @Column(name = "subscribers", nullable = false)
    private int subscribers;
    @Column(name = "subscriptions", nullable = false)
    private int subscriptions;

    User() {
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(Long id, String name, String email, int subscribers, int subscriptions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subscribers = subscribers;
        this.subscriptions = subscriptions;
    }
}