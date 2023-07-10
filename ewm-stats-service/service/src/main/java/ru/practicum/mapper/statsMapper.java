package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.HitDto;
import ru.practicum.StatsDto;
import ru.practicum.formatter.DateFormatter;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

@UtilityClass
public class statsMapper {

    public Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(DateFormatter.toLocalDateTime(hitDto.getTimestamp()))
                .build();
    }

    public HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(DateFormatter.toString(hit.getTimestamp()))
                .build();
    }

    public StatsDto toStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits()).build();
    }

    public Stats toStats(StatsDto statsDto) {
        return Stats.builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .hits(statsDto.getHits()).build();
    }
}