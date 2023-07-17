package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.state.StateAction;

import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventAdminRequestDto {
    Long id;
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    String eventDate;
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}