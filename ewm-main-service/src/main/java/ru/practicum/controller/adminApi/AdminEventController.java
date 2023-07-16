package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Long id,
                               @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {

        return EventMapper.fromEventToEventFullDto(eventService.updateEventByAdmin(id,
                EventMapper.toUpdateEventAdminRequest(updateEventAdminRequestDto)));
    }
}