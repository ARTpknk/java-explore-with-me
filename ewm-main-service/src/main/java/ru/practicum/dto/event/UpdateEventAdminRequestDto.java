package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.state.StateAction;

@Data
@Builder
public class UpdateEventAdminRequestDto {
    Long id;
    String annotation;
    Long category;
    String description;
    String eventDate;
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    String title;
}