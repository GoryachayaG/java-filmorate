package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Like {
    @NonNull
    long filmId;
    @NonNull
    long userId;
}
