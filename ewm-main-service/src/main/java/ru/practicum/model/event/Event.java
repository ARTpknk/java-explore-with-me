package ru.practicum.model.event;

import lombok.*;
import ru.practicum.model.category.Category;
import ru.practicum.model.state.State;
import ru.practicum.model.user.User;
import ru.practicum.model.location.Location;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "annotation", nullable = false)
    String annotation;
    @With
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @With
    @Column(name = "confirmed_requests")
    int confirmedRequests;
    @With
    @Column(name = "created_on")
    LocalDateTime createdOn;
    String description;
    @With
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @With
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    @With
    @Embedded
    Location location;
    @With
    @Column(name = "paid")
    Boolean paid;
    @With
    @Column(name = "participant_limit")
    int participantLimit;
    @With
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @With
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @With
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    State state;
    @Column(name = "title")
    String title;
    @With
    @Transient
    int views;



    public Event() {
    }

    public Event(Long id, String annotation, Category category, int confirmedRequests, LocalDateTime createdOn,
                 String description, LocalDateTime eventDate, User initiator, Location location, Boolean paid,
                 int participantLimit, LocalDateTime publishedOn, Boolean requestModeration, State state, String title,
                 int views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }
}