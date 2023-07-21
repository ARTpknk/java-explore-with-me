package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@Jacksonized
public class EventFilter {
    Long[] users;
    String[] states;
    Long[] categories;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;
    @PositiveOrZero
    Integer from;
    @Positive
    Integer size;
    String text;
    Boolean paid;
    String sort;
    Boolean onlyAvailable;

    @JsonCreator
    public EventFilter(@JsonProperty(value = "users") Long[] users,
                       @JsonProperty(value = "states") String[] states,
                       @JsonProperty(value = "categories") Long[] categories,
                       @JsonProperty(value = "rangeStart")
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                       @JsonProperty(value = "rangeEnd")
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                       @JsonProperty(value = "from") Integer from,
                       @JsonProperty(value = "size") Integer size,
                       @JsonProperty(value = "text") String text,
                       @JsonProperty(value = "paid") Boolean paid,
                       @JsonProperty(value = "sort") String sort,
                       @JsonProperty(value = "onlyAvailable") Boolean onlyAvailable
    ) {
        this.users = users;
        this.states = states;
        this.categories = categories;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.from = Objects.requireNonNullElse(from, 0);
        this.size = Objects.requireNonNullElse(size, 10);
        this.text = text;
        this.paid = paid;
        this.sort = sort;
        this.onlyAvailable = Objects.requireNonNullElse(onlyAvailable, false);
    }
}