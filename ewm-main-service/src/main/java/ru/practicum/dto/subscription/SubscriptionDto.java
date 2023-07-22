package ru.practicum.dto.subscription;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SubscriptionDto {
    private Long id;
    @NotNull
    private UserDto subscriber;
    @NotNull
    private UserDto creator;   //создатель объявлений
}