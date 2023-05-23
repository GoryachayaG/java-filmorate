package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос к эндпоинту PUT для обновления данных фильма с id = {}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос к эндпоинту GET для получения списка всех фильмов");
        return filmService.findAllFilms();
    }

    //С помощью аннотации @PathVariable добавьте возможность получать каждый фильм и данные о пользователях
    // по их уникальному идентификатору: GET .../users/{id}
    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable int filmId) {
        return filmService.findFilmById(filmId);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту PUT от пользователя с id = {}, " +
                "для добавления лайка фильму с id = {}", userId, id);
        return filmService.addLike(id, userId);
    }

    //пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту DELETE от пользователя с id = {}, " +
                "для удаления лайка с фильма с id = {}", userId, id);
        return filmService.removeLike(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping(value = {"/popular?count={count}", "/popular"})
    public List<Film> findTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }
}
