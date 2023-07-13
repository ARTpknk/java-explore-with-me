package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public abstract class BaseClient {
    WebClient webClient = WebClient.create("http://localhost:9090");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void postRequest(String app, String path, String ip, LocalDateTime time) {
        HitDto dto = HitDto.builder()
                .app(app)
                .uri(path)
                .ip(ip)
                .timestamp(time.format(formatter))
                .build();

        webClient
                .post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", start.format(formatter))
                                .queryParam("end", end.format(formatter))
                                .queryParam("uris", uri)
                                .queryParam("unique", unique)
                                .build())
                .retrieve()
                .bodyToFlux(StatsDto.class)
                .collectList()
                .block();
    }
}