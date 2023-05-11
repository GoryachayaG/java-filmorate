package ru.yandex.practicum.filmorate.storages.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException(String.format("Пользователя с id %d  нет в базе", user.getId()));
        }
        return user;
    }

    public void remove(User user) {
        users.remove(user.getId());
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUserById(long userId) {
        return getUsers().stream().filter(x -> x.getId() == userId).findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", userId)));
    }
}
