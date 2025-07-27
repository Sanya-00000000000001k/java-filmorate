package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void testFindAll_EmptyList() {
        Collection<Film> films = filmController.findAll();
        assertTrue(films.isEmpty());
    }

    @Test
    void testAddFilm_ValidFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Film addedFilm = filmController.addFilm(film);

        assertNotNull(addedFilm.getId());
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void testAddFilm_InvalidName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    void testAddFilm_DescriptionTooLong() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Длина описания фильма больше 200 символов"));
    }

    @Test
    void testAddFilm_InvalidReleaseDate() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(1895, 10, 27));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Дата создания фильма не может быть раньше 28-10-1895"));
    }

    @Test
    void testAddFilm_NegativeDuration() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(-100);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
    }

    @Test
    void testUpdateFilm_ValidFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film addedFilm = filmController.addFilm(film);

        addedFilm.setDescription("Updated description");
        Film updatedFilm = filmController.updateFilm(addedFilm);

        assertEquals("Updated description", updatedFilm.getDescription());
    }

    @Test
    void testUpdateFilm_MissingId() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertEquals("Требуется указать id фильма", exception.getMessage());
    }

    @Test
    void testUpdateFilm_NonExistentId() {
        Film film = new Film();
        film.setId(999L); // Non-existent ID
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
        assertTrue(exception.getMessage().contains("Фильм с указанным ID не найден"));
    }
}