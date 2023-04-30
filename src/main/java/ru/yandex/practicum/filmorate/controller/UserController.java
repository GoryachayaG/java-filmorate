package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap();

    private int id = 0;

    private int generateId() { // Генерирует новый Id
        return ++id;
    }

    //создание пользователя; - присваивается id
    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту PUT для создания пользователя с id = {}", id + 1);
        if (isValid(user)) {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("Пользователь с id = {} успешно создан", user.getId());
        }
        return user;
    }

    //обновление пользователя; - обновить можно все, кроме id
    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту PUT для обновления данных пользователя с id = {}", user.getId());
        if (users.containsKey(user.getId())) {
            if (isValid(user)) {
                //&& users.containsKey(user.getId())
                users.put(user.getId(), user);
                log.info("Пользователь с id = {} успешно обновлен", user.getId());
            } else {
                throw new ValidationException("Такого пользователя не существует");
            }
        } else {
            throw new ValidationException("Пользователя с таким id нет в базе");
        }

        return user;
    }

    //получение списка всех пользователей.
    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос к эндпоинту GET для получения списка всех пользователей");
        return users.values();
    }

    private boolean isValid(User user) {
        if (user == null) {
            log.warn("Валидация не пройдена. user == null");
            throw new NullPointerException("user == null. Нужно заполнить поля");
        }
        if (user.getEmail().equals("")) {
            log.warn("Валидация не пройдена. Поле эл. почты пустое");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Валидация не пройдена. Адрес эл. почты не содержит символ @");
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin().isBlank()) {
            log.warn("Валидация не пройдена. Поле логин пустое");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().indexOf(' ') >= 0) {
            log.warn("Валидация не пройдена. Логин {} содержит пробелы", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Валидация не пройдена. Дата рождения еще не наступила");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Имя пользователя не указано. Используем логин");
            user.setName(user.getLogin());
        }

        return true;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }
}
