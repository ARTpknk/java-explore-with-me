package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query(value = "SELECT new ru.practicum.model.Stats(app, uri, COUNT(DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.uri in :uris " +
            "AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stats> findUniqueWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.model.Stats(app, uri, COUNT(DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stats> findUniqueWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.model.Stats(app, uri, COUNT(h.ip)) FROM Hit AS h " +
            "WHERE h.uri in :uris " +
            "AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stats> findNotUniqueWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.model.Stats(app, uri, COUNT(h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<Stats> findNotUniqueWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}