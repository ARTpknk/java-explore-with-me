package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void create(Hit hit);

    List<Stats> get(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique);
}
