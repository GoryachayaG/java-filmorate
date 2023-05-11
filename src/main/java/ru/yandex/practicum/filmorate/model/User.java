package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NonNull
    String login;
    String name;
    long id;
    @NonNull
    String email;
    @NonNull
    LocalDate birthday;

    Set<Long> friends = new HashSet<>(); //список id пользователей, которые есть в друзьях

    // нужен конструктор с name, login, email, b-day
    public User(@NonNull String login, String name, @NonNull String email, @NonNull LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    // нужен конструктор с login, email, b-day - автоматически создался

}
