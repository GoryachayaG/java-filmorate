package ru.yandex.practicum.filmorate.storages.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllRatings() {
        String sqlQuery = "SELECT * FROM rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    @Override
    public Mpa getRatingById(int ratingId) {
        String sqlQuery = "SELECT * FROM rating WHERE rating_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, ratingId);
        } catch (RuntimeException e) {
            throw new RatingNotFoundException(String.format("Рейтинга с id %d  нет в базе", ratingId));
        }
    }

    private Mpa mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
