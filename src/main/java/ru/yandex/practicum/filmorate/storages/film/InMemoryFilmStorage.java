package ru.yandex.practicum.filmorate.storages.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();


    //добавление фильма
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException("Фильма с таким id нет в базе");
        }
        return film;
    }

    //получение всех фильмов
    public Collection<Film> findAll() {
        return films.values();
    }

    //предыдущий метод дублирует суть - только нужно обдумать, можно ли получить по номеру элемента, а не ай ди
    // получить мапу с фильмами из хранилища
    public Map<Long, Film> getFilms() {
        return films;
    }
}
