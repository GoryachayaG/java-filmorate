package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void remove(Film film);

    Collection<Film> getFilms();

    public Film getFilmById(long filmId);
}
