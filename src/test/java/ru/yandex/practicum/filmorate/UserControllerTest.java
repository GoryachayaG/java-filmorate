package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    protected UserService service = new UserService();

    @Test
    public void createValidUser() {
        User user = new User("login", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));
        service.createUser(user);
        assertEquals(user, service.findUserById(1));
    }

    @Test
    public void createUserWithInvalidEmail() {
        User user = new User("login", "my name", "mymail", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createUser(user)
        );

        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyEmail() {
        User user = new User("login", "my name", "", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createUser(user)
        );

        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    public void createUserWithInvalidLogin() {
        User user = new User("login rr", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createUser(user)
        );

        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    public void createUserWithEmptyLogin() {
        User user = new User(" ", "my name", "mymail@mail.ru", LocalDate.parse("1995-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createUser(user)
        );

        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    public void createUserWithoutName() {
        User user = new User("login", "", "mymail@mail.ru", LocalDate.parse("1995-07-16"));
        service.createUser(user);
        assertEquals(user.getLogin(), service.findUserById(1).getName());
    }

    @Test
    public void createUserWithInvalidData() {
        User user = new User("login", "my name", "mymail@mail.ru", LocalDate.parse("2027-07-16"));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createUser(user)
        );

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    public void createEmptyUser() {
        User user = null;

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.createUser(user)
        );

        assertEquals("user == null. Нужно заполнить поля", exception.getMessage());
    }
}
