package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.service.request.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable("userId") Long userId,
                                                 @RequestParam("eventId") Long eventId) {
        return RequestMapper.toParticipationRequestDto(requestService.createRequest(userId, eventId));
    }

    @GetMapping()
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable("userId") Long userId) {
        return requestService.getRequestsOfUser(userId)
                .stream().map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {

        return RequestMapper.toParticipationRequestDto(requestService.cancelRequest(userId, requestId));
    }
}