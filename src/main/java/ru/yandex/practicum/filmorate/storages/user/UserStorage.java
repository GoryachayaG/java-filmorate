package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;


public interface UserStorage {
    User create(User user);

    User update(User user);

    void remove(User user);

    Collection<User> getUsers();

    User getUserById(long userId);

    List<User> getCommonFriends(long id, long otherId);
    List<User> getFriendsByUserId(long id);
    User addFriend(long userId, long friendId);
    User deleteFriend(long userId, long friendId);
}
