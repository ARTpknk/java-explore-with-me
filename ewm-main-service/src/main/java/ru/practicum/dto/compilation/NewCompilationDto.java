package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    Long id;
    Set<Long> events;
    Boolean pinned;
    @Size(max = 50)
    String title;

    public NewCompilationDto(Long id, Set<Long> events, Boolean pinned, String title) {
        this.id = id;
        this.events = events;
        this.pinned = Objects.requireNonNullElse(pinned, false);
        this.title = title;
    }
}
