package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void remove(Film film);

    Collection<Film> getFilms();

    public Film getFilmById(long filmId);

    List<Film> getTopFilms(Integer count);

    void updateGenres(long filmId, Set<Genre> genres);

    void addGenres(long filmId, Set<Genre> genres);

    Set<Genre> findGenres(long filmId);
}
