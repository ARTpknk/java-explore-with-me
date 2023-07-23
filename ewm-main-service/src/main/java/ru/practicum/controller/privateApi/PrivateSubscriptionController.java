package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.SubscriptionMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.service.subscription.SubscriptionService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/subscription")
@RequiredArgsConstructor
public class PrivateSubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/{creatorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribe(@PathVariable("userId") Long subscriberId,
                                     @PathVariable("creatorId") Long creatorId) {
        return SubscriptionMapper.toSubscriptionDto(subscriptionService.subscribe(subscriberId, creatorId));
    }

    @DeleteMapping("/{creatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable("userId") Long subscriberId,
                            @PathVariable("creatorId") Long creatorId) {
        subscriptionService.unsubscribe(subscriberId, creatorId);
    }

    @GetMapping
    public List<UserDto> getSubscribes(@PathVariable("userId") Long subscriberId,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        return subscriptionService.getSubscribes(subscriberId, from, size)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/events")
    public List<EventShortDto> getSubscribedEvents(@PathVariable("userId") Long subscriberId,
                                                   @RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return subscriptionService.getSubscribedEvents(subscriberId, from, size)
                .stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/subscribers")
    public List<UserDto> getSubscribers(@PathVariable("userId") Long creatorId,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        return subscriptionService.getSubscribers(creatorId, from, size)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}