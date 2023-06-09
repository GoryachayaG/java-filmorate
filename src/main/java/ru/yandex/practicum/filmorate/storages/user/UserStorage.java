package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {
    User create(User user);

    User update(User user);

    void remove(User user);

    Collection<User> getUsers();

    User getUserById(long userId);
}
