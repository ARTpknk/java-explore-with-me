package ru.practicum.dto.subscription;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SubscriptionDto {
    private Long id;
    @NotNull
    private UserShortDto subscriber;
    @NotNull
    private UserShortDto creator;   //создатель объявлений
}