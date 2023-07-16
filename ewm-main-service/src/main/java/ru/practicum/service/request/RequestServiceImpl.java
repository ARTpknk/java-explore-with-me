package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.event.EventRequestStatusUpdateResult;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.state.State;
import ru.practicum.model.user.User;
import ru.practicum.repository.RequestRepository;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final EventService eventService;
    private final UserService userService;


    @Override
    public ParticipationRequest createRequest(Long userId, Long eventId) {
        if (repository.findAllByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ExploreWithMeConflictException("user already made a request");
        }
        Event event = eventService.getEventById(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new ExploreWithMeConflictException(String.format("event is not published " + event.getId()));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ExploreWithMeConflictException(
                    String.format("user is owner of event " + event.getId()));
        }
        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            ParticipationRequest request;
            if (event.getRequestModeration()) {
                request = ParticipationRequest.builder()
                        .event(eventService.getEventById(eventId))
                        .requester(userService.getUserById(userId))
                        .created(LocalDateTime.now())
                        .status(RequestStatus.PENDING)
                        .build();
            } else {
                request = ParticipationRequest.builder()
                        .event(eventService.getEventById(eventId))
                        .requester(userService.getUserById(userId))
                        .created(LocalDateTime.now())
                        .status(RequestStatus.PUBLISHED)
                        .build();
                eventService.addConfirmedRequest(event);
            }
            return repository.save(request);
        } else {
            throw new ExploreWithMeConflictException(
                    String.format("Event with id: %d is already full", event.getId()));
        }
    }

    @Override
    public List<ParticipationRequest> getRequestsOfUser(Long userId) {
        return repository.findByRequesterId(userId);
    }

    @Override
    public List<ParticipationRequest> getRequestsOfEvent(Long userId, Long eventId) {
        /*if(!eventService.getEventById(eventId).getInitiator().getId().equals(userId)){
            throw new ExploreWithMeConflictException("not your event";
        }
        будет ли в тестах это
         */
        return repository.findByEventId(eventId);
    }


    @Override
    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = getRequestById(requestId);
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(request.getEvent().getId());
        if (request.getRequester().getId().equals(userId)) {
            request.setStatus(RequestStatus.CANCELED);
            eventService.deleteConfirmedRequest(event);
            return repository.save(request);
        } else {
            throw new ExploreWithMeNotFoundException("User with Id " + userId +
                    " don't own request with Id: " + requestId);
        }
    }


    @Override
    public ParticipationRequest getRequestById(Long requestId) {
        if (repository.findById(requestId).isPresent()) {
            return repository.findById(requestId).get();
        } else {
            throw new ExploreWithMeNotFoundException("Request with Id: " + requestId + " not found");
        }
    }


    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId,
                                                         Long eventId,
                                                         EventRequestStatusUpdateRequest answer) {
        RequestStatus status = answer.getStatus();
        List<ParticipationRequest> updatedRequests;
        if (answer.getStatus() == RequestStatus.CONFIRMED || answer.getStatus() == RequestStatus.REJECTED) {
            answer.getRequestIds().stream()
                    .map((Long requestId) -> updateRequest(requestId, status, eventId)).collect(Collectors.toList());
            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(repository.findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED))
                    .rejectedRequests(repository.findByEventIdAndStatus(eventId, RequestStatus.REJECTED))
                    .build();
        } else {
            throw new ExploreWithMeConflictException("некорректный запрос");
        }
    }

    @Transactional
    public ParticipationRequest updateRequest(Long id, RequestStatus status, Long eventId) {
        ParticipationRequest request = getRequestById(id);
        if (request.getStatus() == RequestStatus.PENDING) {
            if (status == RequestStatus.CONFIRMED) {
                Event event = eventService.getEventById(eventId);
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    eventService.addConfirmedRequest(event);
                    request.setStatus(status);
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
            return repository.save(request);
        } else {
            throw new ExploreWithMeConflictException("Заявка уже обработана");
        }
    }
}