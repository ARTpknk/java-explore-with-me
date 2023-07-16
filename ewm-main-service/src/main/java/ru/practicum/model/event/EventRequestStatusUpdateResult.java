package ru.practicum.model.event;

import lombok.Builder;
import lombok.Value;
import ru.practicum.model.request.ParticipationRequest;

import java.util.List;

@Value
@Builder
public class EventRequestStatusUpdateResult {
    List<ParticipationRequest> confirmedRequests;
    List<ParticipationRequest> rejectedRequests;
}