package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequestDto {
    List<Long> requestIds;
    RequestStatus status;
}