package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.model.*;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

public NewEvent fromNewEventDtoToNewEvent(NewEventDto newEventDto){
    return NewEvent.builder()
            .annotation(newEventDto.getAnnotation())
            .category(newEventDto.getCategory())
            .description(newEventDto.getDescription())
            .eventDate(newEventDto.getEventDate())
            .location(toLocation(newEventDto.getLocation()))
            .paid(newEventDto.isPaid())
            .participantLimit(newEventDto.getParticipantLimit())
            .requestModeration(newEventDto.isRequestModeration())
            .title(newEventDto.getTitle())
            .build();
}

    public Event fromNewEventToEvent(NewEvent newEvent, Category category, User user, LocalDateTime createdOn){
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(newEvent.getDescription())
                .eventDate(createdOn)
                .initiator(user)
                .location(newEvent.getLocation())
                .paid(newEvent.getPaid())
                .participantLimit(newEvent.getParticipantLimit())
                .requestModeration(newEvent.getRequestModeration())
                .state(State.PENDING)
                .title(newEvent.getTitle())
                .views(0)
                .build();
    }

    public EventFullDto fromEventToEventFullDto(Event event){
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(DateFormatter.toString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto fromEventToEventShortDto(Event event){
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
    Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
    LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}