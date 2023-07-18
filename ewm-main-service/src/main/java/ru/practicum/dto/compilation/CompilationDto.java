package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    Long id;
    Set<EventShortDto> events;
    Boolean pinned;
    String title;
}
