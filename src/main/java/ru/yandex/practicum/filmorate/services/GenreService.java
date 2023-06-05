package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storages.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getAllGenres() {
        log.info("Получен запрос к эндпоинту GET для получения всех жанров");

        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(int genreId) {
        log.info("Получен запрос к эндпоинту GET для получения жанра с id = {}", genreId);

        contains(genreId);
        return genreStorage.getGenreById(genreId);
    }

    private void contains(int genreId) {
        try {
            genreStorage.getGenreById(genreId);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Жанр с id {} не найден", genreId);
            throw new GenreNotFoundException("Жанр не найден");
        }
    }

}
