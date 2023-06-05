package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.services.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public Collection<Mpa> findAllRatings() {
        log.info("Получен запрос к эндпоинту GET для получения всех видов рейтинга");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Mpa findRating(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту GET для получения рейтинга с id = {}", id);
        return ratingService.getRatingById(id);
    }
}
