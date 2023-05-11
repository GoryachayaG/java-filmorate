package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exeptions.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService() {
        this.userStorage = new InMemoryUserStorage();
    }

    private int id = 0;

    private int generateId() { // Генерирует новый Id
        return ++id;
    }

    //создание пользователя; - присваивается id
    public User createUser(User user) {
        log.info("Получен запрос к эндпоинту PUT для создания пользователя с id = {}", id + 1);

        if (isValid(user)) {
            user.setId(generateId());
            userStorage.create(user);
            log.info("Пользователь с id = {} успешно создан", user.getId());
        }
        return user;
    }

    public User updateUser(User user) {
        if (isValid(user)) {
            userStorage.update(user);
            log.info("Пользователь с id = {} успешно обновлен", user.getId());
        }
        return user;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAll();
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

    //добавление в друзья
    //пользователям не надо одобрять заявки в друзья — добавляем сразу.
    // То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
    public User addFriend(Long userID, Long friendID) {
        if (!findAllUsers().contains(findUserById(friendID))) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", friendID));
        } else if (!findAllUsers().contains(findUserById(userID))) {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userID));
        } else if (findUserById(userID).getFriends().contains(friendID)) {
            throw new FriendAlreadyAddedException(String.format("Пользователь с id %d " +
                    "уже дружит с пользователем с id %d", userID, friendID));
        } else {
            findUserById(userID).getFriends().add(friendID);
            findUserById(friendID).getFriends().add(userID);
        }
        return findUserById(userID);
    }

    // удаление из друзей
    public User removeFriend(Long userID, Long friendID) {
        findUserById(userID).getFriends().remove(friendID);
        findUserById(friendID).getFriends().remove(userID);
        return findUserById(userID);
    }

    // вывод списка общих друзей
    public List<User> getCommonFriends(Long userID, Long otherID) {
        List<User> commonFriends = new ArrayList<>();
        for (Long friendID : findUserById(userID).getFriends()) {
            if (findUserById(otherID).getFriends().contains(friendID)) {
                commonFriends.add(findUserById(friendID));
            }
        }
        return commonFriends;
    }

    //возвращаем список пользователей, являющихся его друзьями
    public List<User> getFriends(Long id) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : findUserById(id).getFriends()) {
            friends.add(findUserById(friendId));
        }
        return friends;
    }

    public User findUserById(long userId) {
        return userStorage.findAll().stream()
                .filter(x -> x.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", userId)));
    }
}
