package ru.practicum.model.compilation;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UpdateCompilation {
    Set<Long> events;
    Boolean pinned;
    String title;




}
