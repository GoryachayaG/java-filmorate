package ru.yandex.practicum.filmorate.storages.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue());
        return user;
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());
        return values;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "name=?, login=?, email=?, birthday=? " +
                "WHERE id=?";
        int rowsCount = jdbcTemplate.update(sqlQuery,  user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        if (rowsCount > 0) {
            return user;
        }
        throw new UserNotFoundException(String.format("Пользователя с id %d  нет в базе", user.getId()));
    }

    @Override
    public void remove(User user) {
        String sqlQuery = "DELETE from users where id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public List<User> getFriendsByUserId(long id) {
        String sqlQuery = "SELECT id, name, login, email, birthday " +
                "FROM users WHERE id IN" +
                "(SELECT friend_id FROM friends WHERE user_id=?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser, id));
    }

    @Override
    public User getUserById(long userId) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
        } catch (RuntimeException e) {
            throw new UserNotFoundException(String.format("Пользователя с id %d  нет в базе", userId));
        }
    }

    public User addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        try {
            getUserById(friendId);
        } catch (RuntimeException e) {
            throw new UserNotFoundException(String.format("Пользователя с id %d  нет в базе", friendId));
        }
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        User user = getUserById(userId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return user;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        String sqlQuery = "SELECT * FROM users WHERE id IN(" +
                "SELECT friend_id FROM friends WHERE user_id = ?) " +
                "AND id IN(SELECT friend_id FROM friends WHERE user_id = ?)";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId));
    }
}
