package ru.practicum.model;

import lombok.Builder;
import lombok.Value;

@Value
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