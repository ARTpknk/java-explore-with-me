package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.subscription.SubscriptionDto;
import ru.practicum.model.subscription.Subscription;

@UtilityClass
public class SubscriptionMapper {

    public SubscriptionDto toSubscriptionDto(Subscription subscription){
        return SubscriptionDto.builder().
                id(subscription.getId())
                .subscriber(UserMapper.toUserDto(subscription.getSubscriber()))
                .creator(UserMapper.toUserDto(subscription.getCreator()))
                .build();
    }
}