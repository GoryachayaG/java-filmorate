package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    protected FilmController controller = new FilmController();

    @Test
    public void createValidFilm() {
        Film film = new Film("film", "about this film", LocalDate.parse("2022-01-01"), 40);
        controller.create(film);
        assertEquals(film, controller.getFilms().get(1));
    }

    @Test
    public void createFilmWithEmptyName() {
        Film film = new Film(" ", "about this film", LocalDate.parse("2022-01-01"), 40);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void createFilmWithLongDescription() {
        Film film = new Film("film", "about this film a lot of symbols here blablablalalalalalaallalalalalladhdhdh\n" +
                " ghghjd fkfdkdfj dkjfjkdjfkdjfkjgkdjfgjkg dkjgkjdjgjdkjgjdkkjgdg dfjkghdfhgkfhgfkg dkfghdkghkfkg \n" +
                " ghghjd fkfdkdfj dkjfjkdjfkdjfkjgkfffffffffgggggggggggggggggg \n", LocalDate.parse("2022-01-01"), 40);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    public void createFilmWithInvalidDate() {
        Film film = new Film("film", "about this film", LocalDate.parse("1800-01-01"), 40);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void createFilmWithInvalidDurationIs0() {
        Film film = new Film("film", "about this film", LocalDate.parse("2000-01-01"), 0);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void createFilmWithNegativeDuration() {
        Film film = new Film("film", "about this film", LocalDate.parse("2000-01-01"), -100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void createEmptyFilm() {
        Film film = null;

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> controller.create(film)
        );

        assertEquals("film == null. Нужно заполнить поля", exception.getMessage());
    }
}
