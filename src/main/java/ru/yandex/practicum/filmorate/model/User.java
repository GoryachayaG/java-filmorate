package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotBlank
    private String login;
    String name;
    long id;
    @NotEmpty
    String email;
    @NotNull
    LocalDate birthday;
    Set<Long> friends; //список id пользователей, которые есть в друзьях
}
