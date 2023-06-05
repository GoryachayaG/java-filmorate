package ru.yandex.practicum.filmorate.storages.like;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(long filmId, long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void remove(long filmId, long userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
