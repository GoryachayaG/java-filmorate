package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.like.LikeStorage;
import ru.yandex.practicum.filmorate.storages.rating.RatingStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier
    private final FilmStorage filmStorage;
    private final RatingStorage ratingMpa;
    private final LikeStorage likeStorage;
    private final UserService userService;
    LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private long id = 0;

    private long generateId() { // Генерирует новый Id
        return ++id;
    }

    public Film createFilm(Film film) {
        log.info("Получен запрос к эндпоинту POST для создания фильма с id = {}", id + 1);
        Film thisFilm = null;
        if (checkValidity(film)) {
            film.setId(generateId());
            thisFilm = filmStorage.create(film);
            if (film.getGenres() != null) {
                filmStorage.addGenres(thisFilm.getId(), film.getGenres());
                thisFilm.setGenres(filmStorage.findGenres(thisFilm.getId()));
            }
        }
        return thisFilm;
    }

    public Film updateFilm(Film film) {
        contains(film.getId());
        Film thisFilm = null;
        if (checkValidity(film)) {
            thisFilm = filmStorage.update(film);
            if (film.getGenres() != null) {
                filmStorage.updateGenres(thisFilm.getId(), film.getGenres());
                thisFilm.setGenres(filmStorage.findGenres(thisFilm.getId()));
            }
            thisFilm.setMpa(ratingMpa.getRatingById(thisFilm.getMpa().getId()));
            log.info("Фильм с id = {} успешно обновлен", film.getId());
        }
        return thisFilm;
    }

    public Collection<Film> findAllFilms() {
        Collection<Film> films = filmStorage.getFilms();
        films.forEach(film -> {
            film.setGenres(filmStorage.findGenres(film.getId()));
            film.setMpa(ratingMpa.getRatingById(film.getMpa().getId()));
        });
        return films;
    }

    public Film findFilmById(long filmId) {
        Film result = contains(filmId);
        result.setGenres(filmStorage.findGenres(filmId));
        result.setMpa(ratingMpa.getRatingById(result.getMpa().getId()));
        return result;
    }

    // Если все ок, метод вернет true. Если хоть одно из условий верно, метод выкинет исключение
    private boolean checkValidity(Film film) {
        if (film == null) {
            log.warn("Валидация не пройдена. film == null");
            throw new NullPointerException("film == null. Нужно заполнить поля");
        }
        if (film.getName().isBlank()) {
            log.warn("Валидация не пройдена. Поле названия фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Валидация не пройдена. Длина описания {} символов", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(cinemaBirthday)) {
            log.warn("Валидация не пройдена. Указанная дата релиза раньше допустимой");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Валидация не пройдена. Продолжительность фильма отрицательная или равна нулю");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }

    private Film contains(long filmId) {
        try {
            return filmStorage.getFilmById(filmId);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Фильм с id {} не найден", filmId);
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    // каждый пользователь может поставить лайк фильму только один раз.
    public Film addLike(Long filmId, Long userId) {
        contains(filmId);
        userService.findUserById(userId);
        likeStorage.add(filmId, userId);
        return findFilmById(filmId);
    }

    // удаление лайка
    public Film removeLike(Long filmId, Long userId) {
        contains(filmId);
        userService.findUserById(userId);
        likeStorage.remove(filmId, userId);
        return findFilmById(filmId);
    }

    // вывод 10 наиболее популярных фильмов по количеству лайков.
    public List<Film> getTopFilms(Integer count) {
        List<Film> result = filmStorage.getTopFilms(count);
        result.forEach(film -> {
            film.setGenres(filmStorage.findGenres(film.getId()));
            film.setMpa(ratingMpa.getRatingById(film.getMpa().getId()));
        });
        return result;
    }
}
