package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Long id,
                               @Valid @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {

        return EventMapper.fromEventToEventFullDto(eventService.updateEventByAdmin(id,
                EventMapper.toUpdateEventRequest(updateEventAdminRequestDto)));
    }

    @GetMapping()
    public List<EventFullDto> getEvents(@Valid EventFilter eventFilter) {
        return eventService.getAllEventsByAdmin(eventFilter).stream()
                .map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }
}