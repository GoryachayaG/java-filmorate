package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .id(1)
                .login("login")
                .name("name")
                .email("email@email.ru")
                .birthday(LocalDate.of(2000, 1, 11))
                .build();
    }

    @Test
    void findAllUsers() {
        Collection<User> users = new ArrayList<>(Collections.singleton(user));

        userStorage.create(user);
        Collection<User> usersActual = userStorage.getUsers();

        assertEquals(users, usersActual);
    }

    @Test
    void createUser() {
        User userActual = userStorage.create(user);

        assertEquals(user, userActual);
    }

    @Test
    void updateUser() {
        userStorage.create(user);
        user.setName("new Name");
        user.setLogin("newLogin");

        User userActual = userStorage.update(user);

        assertEquals(user, userActual);
    }

    @Test
    void findUserById() {
        userStorage.create(user);

        User userActual = userStorage.getUserById(1);

        assertEquals(user, userActual);
    }
}