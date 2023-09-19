package ru.practicum.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFilter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@Valid EventFilter eventFilter, HttpServletRequest httpServletRequest) {
        return eventService.getAllEventsByPublic(eventFilter, httpServletRequest.getRequestURI(),
                        httpServletRequest.getRemoteAddr()).stream()
                .map(EventMapper::fromEventToEventFullDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        return EventMapper.fromEventToEventFullDto(eventService.getEventByIdByPublicRequest(id,
                httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr()));
    }
}