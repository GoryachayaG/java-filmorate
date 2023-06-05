package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive
    int duration;
    Set<Long> likes; // список id пользователей, которые лайкнули фильм
    Set<Genre> genres;
    @NotNull
    Mpa mpa;
}
