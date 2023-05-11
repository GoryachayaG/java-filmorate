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

    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException(String.format("Фильма с id %d  нет в базе", film.getId()));
        }
        return film;
    }

    public void remove(Film film) {
        films.remove(film.getId());
    }

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film getFilmById(long filmId) {
        return getFilms().stream().filter(x -> x.getId() == filmId).findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", filmId)));
    }
}
