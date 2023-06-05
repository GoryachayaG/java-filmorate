package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Friends {
    @NonNull
    long friendId;
    @NonNull
    long userId;
    @NonNull
    boolean friendship;
}
