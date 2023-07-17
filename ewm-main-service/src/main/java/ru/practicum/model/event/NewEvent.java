package ru.practicum.model.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.location.Location;

@Data
@Builder
public class NewEvent {
    String annotation;
    Long category;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
}