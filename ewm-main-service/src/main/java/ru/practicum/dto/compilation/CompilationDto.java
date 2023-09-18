package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CompilationDto {
    Long id;
    Set<EventShortDto> events;
    Boolean pinned;
    @NotBlank
    @Size(max = 50)
    String title;
}