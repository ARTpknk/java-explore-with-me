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
public interface  StatsRepository extends JpaRepository<Hit, Integer> {

    @Query(value = "SELECT new ru.practicum.model.Stats(app, uri, COUNT(DISTINCT e.ip)) FROM Hit AS e " +
            "WHERE e.uri in :uris " +
            "AND e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<Stats> findUniqueWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(distinct h.ip)) from Hit h " +
            "where h.timestamp between :start and :end " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> findUniqueWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(h.ip)) from Hit h " +
            "where h.uri in :uris and h.timestamp between :start and :end " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> findNotUniqueWithUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(h.ip)) from Hit h " +
            "where h.timestamp between :start and :end " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> findNotUniqueWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}