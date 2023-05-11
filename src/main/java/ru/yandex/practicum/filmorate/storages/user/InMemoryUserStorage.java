package ru.yandex.practicum.filmorate.storages.user;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();

    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException("Пользователя с таким id нет в базе");
        }
        return user;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    //нужно ли
    public Map<Long, User> getUsers() {
        return users;
    }
}
