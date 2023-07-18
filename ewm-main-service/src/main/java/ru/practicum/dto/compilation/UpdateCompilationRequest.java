package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class UpdateCompilationRequest {
    Set<Long> events;
    Boolean pinned;
    @Size(max = 50)
    String title;

    public UpdateCompilationRequest(Set<Long> events, Boolean pinned, String title) {
        this.events = events;
        this.pinned = Objects.requireNonNullElse(pinned, false);
        this.title = title;
    }
}
