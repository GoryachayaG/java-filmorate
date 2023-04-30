package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    protected UserController controller = new UserController();

    @Test
    public void createValidUser() {
        User user = new User("login", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));
        controller.create(user);
        assertEquals(user, controller.getUsers().get(1));
    }

    @Test
    public void createUserWithInvalidEmail() {
        User user = new User("login", "my name", "mymail", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyEmail() {
        User user = new User("login", "my name", "", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    public void createUserWithInvalidLogin() {
        User user = new User("login rr", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyLogin() {
        User user = new User(" ", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    public void createUserWithoutName() {
        User user = new User("login", "", "mymail@mail.ru", LocalDate.parse("1995-07-16"));
        controller.create(user);
        assertEquals(user.getLogin(), controller.getUsers().get(1).getName());
    }

    @Test
    public void createUserWithInvalidData() {
        User user = new User("login", "my name", "mymail@mail.ru", LocalDate.parse("2027-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    public void createEmptyUser() {
        User user = null;

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> controller.create(user)
        );

        assertEquals("user == null. Нужно заполнить поля", exception.getMessage());
    }
}
