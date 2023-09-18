package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.*;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.*;
import ru.practicum.model.location.Location;
import ru.practicum.model.state.State;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {
    @Transactional
    public NewEvent fromNewEventDtoToNewEvent(NewEventDto newEventDto) {
        return NewEvent.builder()
                .annotation(newEventDto.getAnnotation())
                .category(newEventDto.getCategory())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(toLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    @Transactional
    public Event fromNewEventToEvent(NewEvent newEvent, Category category, User user, LocalDateTime createdOn) {
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

    @Transactional
    public EventFullDto fromEventToEventFullDto(Event event) {
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
                .publishedOn(event.getPublishedOn() == null ?
                        null : event.getPublishedOn().toString())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }


    @Transactional
    public EventShortDto fromEventToEventShortDto(Event event) {
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

    @Transactional
    public UpdateEventRequest toUpdateEventRequest(UpdateEventRequestDto dto) {
        return UpdateEventRequest.builder()
                .id(dto.getId())
                .annotation(dto.getAnnotation())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation() == null ?
                        null : toLocation(dto.getLocation()))
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .stateAction(dto.getStateAction())
                .title(dto.getTitle())
                .build();
    }

    @Transactional
    public Event updatingEventByAdmin(Event event, Category category, UpdateEventRequest updateRequest) {
        event.setCategory(category);
        if (updateRequest.getAnnotation() != null && !updateRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().isBlank()) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(DateFormatter.toLocalDateTime(updateRequest.getEventDate()));
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(updateRequest.getLocation());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(Math.toIntExact(updateRequest.getParticipantLimit()));
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isBlank()) {
            event.setTitle(updateRequest.getTitle());
        }
        return event;
    }

    @Transactional
    public Event updatingEventByUser(Event event, Category category, UpdateEventRequest updateRequest) {
        event.setCategory(category);
        if (updateRequest.getAnnotation() != null && !updateRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().isBlank()) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(DateFormatter.toLocalDateTime(updateRequest.getEventDate()));
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(updateRequest.getLocation());
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(Math.toIntExact(updateRequest.getParticipantLimit()));
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isBlank()) {
            event.setTitle(event.getTitle());
        }
        return event;
    }

    @Transactional
    public EventRequestStatusUpdateRequest
    toEventRequestStatusUpdateRequest(EventRequestStatusUpdateRequestDto dto) {
        return EventRequestStatusUpdateRequest.builder()
                .requestIds(dto.getRequestIds())
                .status(dto.getStatus())
                .build();
    }

    @Transactional
    public EventRequestStatusUpdateResultDto
    toEventRequestStatusUpdateResultDto(EventRequestStatusUpdateResult result) {
        return EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(result.getConfirmedRequests()
                        .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                .rejectedRequests(result.getRejectedRequests()
                        .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    @Transactional
    LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}