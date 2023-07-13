package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void create(Hit hit) {
        statsRepository.save(hit);
    }

    @Override
    public List<Stats> get(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        System.out.println("Прошёл контроллер");
        if (unique) {
            if (uri == null) {
                return statsRepository.findUniqueWithoutUris(start, end);
            } else {
                return statsRepository.findUniqueWithUris(start, end, uri);
            }
        } else {
            if (uri == null) {
                return statsRepository.findNotUniqueWithoutUris(start, end);
            } else {
                return statsRepository.findNotUniqueWithUris(start, end, uri);
            }
        }
    }
}