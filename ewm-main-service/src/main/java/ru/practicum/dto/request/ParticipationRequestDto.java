package ru.practicum.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.request.RequestStatus;

@Data
@Builder
public class ParticipationRequestDto {
    Long id;
    Long event;
    Long requester;
    String created;
    RequestStatus status;
}