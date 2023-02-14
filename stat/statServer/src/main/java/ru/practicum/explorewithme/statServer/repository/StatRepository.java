package ru.practicum.explorewithme.statServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.core.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query(value = "" +
            "SELECT " +
                "new ru.practicum.explorewithme.core.dto.StatDto(h.app, h.uri, COUNT(h)) " +
            "FROM " +
                "Hit h " +
            "WHERE " +
                "h.app = :app AND " +
                "h.created > :start AND " +
                "h.created < :end " +
            "GROUP BY " +
                "h.app, h.uri")
    List<StatDto> findByCreated(@Param("app") String app,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    @Query(value = "" +
            "SELECT " +
                "new ru.practicum.explorewithme.core.dto.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM " +
                "Hit h " +
            "WHERE " +
                "h.app = :app AND " +
                "h.created > :start AND " +
                "h.created < :end " +
            "GROUP BY " +
                "h.app, h.uri")
    List<StatDto> findByCreatedWithUniqIp(@Param("app") String app,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    @Query(value = "" +
            "SELECT " +
                "new ru.practicum.explorewithme.core.dto.StatDto(h.app, h.uri, COUNT(h)) " +
            "FROM " +
                "Hit h " +
            "WHERE " +
                "h.app = :app AND " +
                "h.created > :start AND " +
                "h.created < :end AND " +
                "h.uri IN :uris " +
            "GROUP BY " +
                "h.app, h.uri")
    List<StatDto> findByCreatedAndUri(@Param("app") String app,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query(value = "" +
            "SELECT " +
                "new ru.practicum.explorewithme.core.dto.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM " +
                "Hit h " +
            "WHERE " +
                "h.app = :app AND " +
                "h.created > :start AND " +
                "h.created < :end AND " +
                "h.uri IN :uris " +
            "GROUP BY " +
                "h.app, h.uri")
    List<StatDto> findByCreatedAndUriWithUniqIp(@Param("app") String app,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end,
                                                @Param("uris") List<String> uris);
}
