package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    protected FilmService service = new FilmService();

    @Test
    public void createValidFilm() {
        Film film = new Film("film", "about this film", LocalDate.parse("2022-01-01"), 40);
        service.createFilm(film);
        assertEquals(film, service.findFilmById(1));
    }

    @Test
    public void createFilmWithEmptyName() {
        Film film = new Film(" ", "about this film", LocalDate.parse("2022-01-01"), 40);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFilm(film)
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
                () -> service.createFilm(film)
        );

        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    public void createFilmWithInvalidDate() {
        Film film = new Film("film", "about this film", LocalDate.parse("1800-01-01"), 40);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFilm(film)
        );

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void createFilmWithInvalidDurationIs0() {
        Film film = new Film("film", "about this film", LocalDate.parse("2000-01-01"), 0);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFilm(film)
        );

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void createFilmWithNegativeDuration() {
        Film film = new Film("film", "about this film", LocalDate.parse("2000-01-01"), -100);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> service.createFilm(film)
        );

        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void createEmptyFilm() {
        Film film = null;

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.createFilm(film)
        );

        assertEquals("film == null. Нужно заполнить поля", exception.getMessage());
    }
}
