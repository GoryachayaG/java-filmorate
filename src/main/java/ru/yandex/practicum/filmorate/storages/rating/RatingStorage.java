package ru.yandex.practicum.filmorate.storages.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingStorage {
    List<Mpa> getAllRatings();

    Mpa getRatingById(int ratingId);
}
