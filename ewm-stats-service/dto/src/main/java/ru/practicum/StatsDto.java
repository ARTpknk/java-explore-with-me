package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class StatsDto {
    String app;
    String uri;
    Long hits;
}