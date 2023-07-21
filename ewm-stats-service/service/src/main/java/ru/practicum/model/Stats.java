package ru.practicum.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stats {
    String app;
    String uri;
    Long hits;

    public Stats() {
    }

    public Stats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}