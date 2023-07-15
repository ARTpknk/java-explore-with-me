package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.NewEvent;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto, @PathVariable("userId") Long userId) {
        NewEvent newEvent = EventMapper.fromNewEventDtoToNewEvent(newEventDto);
        return EventMapper.fromEventToEventFullDto(eventService.createEvent(userId, newEvent));
    }

    @GetMapping()
    public List<EventShortDto> getEventsOfUser(@PathVariable("userId") Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ExploreWithMeBadRequest("некорректные значения");
        }
        return eventService.getEventsOfUser(userId, from, size)
                .stream().map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable("userId") Long userId,
                                       @PathVariable("eventId") Long eventId) {
        return EventMapper.fromEventToEventFullDto(eventService.getEventOfUser(userId, eventId));
    }
}