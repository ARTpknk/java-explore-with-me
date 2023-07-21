package ru.practicum.dto.location;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LocationDto {
    @NotNull
    Float lat;
    @NotNull
    Float lon;
}