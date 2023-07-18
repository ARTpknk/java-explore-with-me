package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.StatsDto;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.service.StatsService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody HitDto hitDto) {
        statsService.create(StatsMapper.toHit(hitDto));
    }

    @GetMapping("/stats")
    public List<StatsDto> get(@RequestParam String start,
                              @RequestParam String end,
                              @RequestParam(required = false) List<String> uris,
                              @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.get(DateFormatter.toLocalDateTime(start), DateFormatter.toLocalDateTime(end), uris, unique)
                .stream()
                .map(StatsMapper::toStatsDto)
                .collect(Collectors.toList());
    }
}