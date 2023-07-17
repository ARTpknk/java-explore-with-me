package ru.practicum.model.event;

import lombok.Builder;
import lombok.Value;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

@Value
@Builder
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus status;
}
