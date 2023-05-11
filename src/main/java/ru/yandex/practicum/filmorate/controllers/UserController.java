package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен запрос к эндпоинту PUT для обновления данных пользователя с id = {}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос к эндпоинту GET для получения списка всех пользователей");
        return userService.findAllUsers();
    }

    //получать данные о пользователях по их id: GET .../users/{id}
    @GetMapping("/{userId}")
    public User findUser(@PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту GET для получения пользователя с id = {}", userId);
        return userService.findUserById(userId);
    }

    //добавление в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос к эндпоинту PUT от пользователя с id = {}, " +
                "для добавления в друзья пользователя с id = {}", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    //удаление из друзей
    @DeleteMapping("/{userId}/friends/{friendId}")
    public User removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос к эндпоинту DELETE от пользователя с id = {}, " +
                "для удаления из друзей пользователя с id = {}", userId, friendId);
        return userService.removeFriend(userId, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long id) {
        log.info("Получен запрос к эндпоинту GET для получения списка друзей пользователя с id = {}", id);
        return userService.getFriends(id);
    }

    //возвращаем список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос к эндпоинту GET от пользователя с id = {}, " +
                "для получения списка общих друзей с пользователем с id = {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
