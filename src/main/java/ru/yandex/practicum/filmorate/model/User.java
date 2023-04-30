package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    @NonNull
    private String login;

    private String name;
    private int id;
    @NonNull
    private String email;
    @NonNull
    private LocalDate birthday;

    // нужен конструктор с name, login, email, b-day
    public User(@NonNull String login, String name, @NonNull String email, @NonNull LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    // нужен конструктор с login, email, b-day - автоматически создался
}
