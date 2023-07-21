package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserShortDto {
    private Long id;
    @NotBlank
    private String name;
}