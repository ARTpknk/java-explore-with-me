package ru.practicum.formatter;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateFormatter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String toString(LocalDateTime date) {
        return date.format(formatter);
    }
    public LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }
}