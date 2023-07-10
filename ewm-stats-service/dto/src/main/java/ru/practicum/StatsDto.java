package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
public class StatsDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    Long hits;
}