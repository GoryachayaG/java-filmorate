package ru.yandex.practicum.filmorate.storages.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Genre getGenreById(long genreId);

    Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException;
}
