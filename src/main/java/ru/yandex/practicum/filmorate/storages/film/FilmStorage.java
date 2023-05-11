package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    //Создайте интерфейсы FilmStorage, в которых будут определены методы добавления, удаления и модификации объектов.

    Film create(Film film);

    Film update(Film film);

    Collection<Film> findAll();
}
