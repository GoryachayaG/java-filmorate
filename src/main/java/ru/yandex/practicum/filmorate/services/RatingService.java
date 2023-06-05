package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storages.rating.RatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingStorage ratingStorage;

    public List<Mpa> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public Mpa getRatingById(int id) {
        return ratingStorage.getRatingById(id);
    }
}
