package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private int id = 0;

    private int generateId() { // Генерирует новый Id
        return ++id;
    }

    //добавление фильма; - присваивается id
    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос к эндпоинту PUT для создания фильма с id = {}", id + 1);

        if (checkValidity(film)) {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.info("Фильм с id = {} успешно создан", film.getId());
        }
        return film;
    }

    //обновление фильма; - обновить можно все, кроме id
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос к эндпоинту PUT для обновления данных фильма с id = {}", film.getId());
        if (films.containsKey(film.getId())) {
            if (checkValidity(film)) {
                films.put(film.getId(), film);
                log.info("Фильм с id = {} успешно обновлен", film.getId());
            }
        } else {
            throw new ValidationException("Фильма с таким id нет в базе");
        }

        return film;
    }

    //получение всех фильмов.
    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос к эндпоинту GET для получения списка всех фильмов");
        return films.values();
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

    public Map<Integer, Film> getFilms() {
        return films;
    }
}
