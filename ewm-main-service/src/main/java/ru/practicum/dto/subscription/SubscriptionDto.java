package ru.practicum.dto.subscription;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SubscriptionDto {
    private Long id;
    @NotNull
    private Long subscriberId;
    @NotNull
    private Long creatorId;   //создатель объявлений
}