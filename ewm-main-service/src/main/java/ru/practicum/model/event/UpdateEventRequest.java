package ru.practicum.model.event;

import lombok.Builder;
import lombok.Value;
import ru.practicum.model.location.Location;
import ru.practicum.model.state.StateAction;

@Value
@Builder
public class UpdateEventRequest {
    Long id;
    String annotation;
    Long category;
    String description;
    String eventDate;
    Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    String title;
}