package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.LikeAlreadyExistException;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private long id = 0;

    @Autowired
    public FilmService() {
        this.filmStorage = new InMemoryFilmStorage();
    }

    private long generateId() { // Генерирует новый Id
        return ++id;
    }

    public Film createFilm(Film film) {
        log.info("Получен запрос к эндпоинту POST для создания фильма с id = {}", id + 1);

        if (checkValidity(film)) {
            film.setId(generateId());
            filmStorage.create(film);

            log.info("Фильм с id = {} успешно создан", film.getId());
        }
        return film;
    }

    public Film updateFilm(Film film) {
        Film thisFilm = null;
        if (checkValidity(film)) {
            thisFilm = filmStorage.update(film);
            log.info("Фильм с id = {} успешно обновлен", film.getId());
        }
        return thisFilm;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAll();
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

    // добавление лайка
    // каждый пользователь может поставить лайк фильму только один раз.
    public Film addLike(Long filmId, Long userId) {
        if (findFilmById(filmId).getLikes().contains(userId)) {
            throw new LikeAlreadyExistException(String.format("Пользователь с id %d " +
                    "уже поставил лайк фильму с id %d", userId, filmId));
        } else {
            findFilmById(filmId).getLikes().add(userId);
        }
        return findFilmById(filmId);
    }

    // удаление лайка
    public Film removeLike(Long filmId, Long userId) {
        if (!findFilmById(filmId).getLikes().contains(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id %d не найден", userId));
        } else {
            findFilmById(filmId).getLikes().remove(userId);
            return findFilmById(filmId);
        }
    }

    // вывод 10 наиболее популярных фильмов по количеству лайков.
    public List<Film> getTopFilms(Integer count) {
        return filmStorage.findAll().stream().sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }

    //получать каждый фильм по уникальному идентификатору: GET .../users/{id}
    public Film findFilmById(long filmId) {
        return filmStorage.findAll().stream()
                .filter(x -> x.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", filmId)));
    }
}
