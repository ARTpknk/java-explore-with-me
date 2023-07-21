package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.event.NewEvent;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable("userId") Long userId) {
        NewEvent newEvent = EventMapper.fromNewEventDtoToNewEvent(newEventDto);
        return EventMapper.fromEventToEventFullDto(eventService.createEvent(userId, newEvent));
    }

    @GetMapping()
    public List<EventShortDto> getEventsOfUser(@PathVariable("userId") Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return eventService.getEventsOfUser(userId, from, size)
                .stream().map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable("userId") Long userId,
                                       @PathVariable("eventId") Long eventId) {
        return EventMapper.fromEventToEventFullDto(eventService.getEventOfUser(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable("userId") Long userId,
                                          @PathVariable("eventId") Long eventId,
                                          @Valid @RequestBody UpdateEventRequestDto updateEventDto) {

        return EventMapper.fromEventToEventFullDto(eventService.updateEventByUser(userId, eventId,
                EventMapper.toUpdateEventRequest(updateEventDto)));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsOfEvent(@PathVariable("userId") Long userId,
                                                            @PathVariable("eventId") Long eventId) {
        return requestService.getRequestsOfEvent(userId, eventId).stream().map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequest(@RequestBody EventRequestStatusUpdateRequestDto answerDto,
                                                           @PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId) {
        EventRequestStatusUpdateRequest answer = EventMapper.toEventRequestStatusUpdateRequest(answerDto);
        return EventMapper.toEventRequestStatusUpdateResultDto(requestService.updateRequests(userId, eventId, answer));

    }
}