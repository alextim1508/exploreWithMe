package ru.practicum.explorewithme.statServer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.stat.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HitJdbcRepository implements HitRepository {

    public static final String TABLE_NAME = "stats";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Hit save(Hit hit) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        jdbcInsert.withTableName(TABLE_NAME).usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("app", hit.getApp())
                .addValue("uri", hit.getUri())
                .addValue("ip", hit.getIp())
                .addValue("created", hit.getCreated());
        Number id = jdbcInsert.executeAndReturnKey(parameters);
        hit.setId(id.longValue());

        return hit;
    }

    @Override
    public List<StatDto> getStats(
            String appName,
            LocalDateTime start,
            LocalDateTime end,
            Boolean uniqIp,
            @Nullable List<String> uris) {

        final String sql = String.format(
                "SELECT " +
                        "app, " +
                        "uri, " +
                        "COUNT(%s) count " +
                        "FROM %s " +
                        "WHERE " +
                        "created > ? AND created < ? " +
                        "AND app=? " +
                        "%s " +
                        "GROUP BY app, uri",

                uniqIp ? "DISTINCT ip" : "*",
                TABLE_NAME,
                uris == null ? "" : "AND uri IN(?)"
        );

        if (uris == null)
            return jdbcTemplate.query(sql, this::mapRowToStatsOutputDto, start, end, appName);
        return jdbcTemplate.query(sql, this::mapRowToStatsOutputDto, start, end, appName, String.join(", ", uris));
    }

    private StatDto mapRowToStatsOutputDto(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() == 0)
            return null;

        return StatDto.builder()
                .uri(rs.getString("uri"))
                .app(rs.getString("app"))
                .hits(rs.getLong("count"))
                .build();
    }
}
