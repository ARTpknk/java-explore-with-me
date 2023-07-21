package ru.practicum.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
    @With
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    RequestStatus status;

    public ParticipationRequest() {
    }

    public ParticipationRequest(Long id, Event event, User requester, LocalDateTime created, RequestStatus status) {
        this.id = id;
        this.event = event;
        this.requester = requester;
        this.created = created;
        this.status = status;
    }
}