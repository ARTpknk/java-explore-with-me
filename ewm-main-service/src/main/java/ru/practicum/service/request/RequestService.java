package ru.practicum.service.request;

import ru.practicum.model.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.event.EventRequestStatusUpdateResult;
import ru.practicum.model.request.ParticipationRequest;

import java.util.List;

public interface RequestService {
    ParticipationRequest createRequest(Long userId, Long eventId);

    List<ParticipationRequest> getRequestsOfUser(Long userId);

    List<ParticipationRequest> getRequestsOfEvent(Long userId, Long eventId);

    ParticipationRequest cancelRequest(Long userId, Long requestId);

    ParticipationRequest getRequestById(Long requestId);

    EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest answer);
}