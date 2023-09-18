package ru.practicum.model.compilation;

import lombok.*;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    @With
    @JoinTable(
            name = "compilations_events",
            inverseJoinColumns = @JoinColumn(name = "event_id"),
            joinColumns = @JoinColumn(name = "compilation_id")
    )
    Set<Event> events;
    @JoinColumn(name = "pinned")
    Boolean pinned;
    @JoinColumn(name = "title")
    String title;
}