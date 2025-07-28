package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void testFindAllFilms() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        filmController.addFilm(film);

        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
        assertEquals("Inception", films.get(0).getName());
    }

    @Test
    void testAddFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Film addedFilm = filmController.addFilm(film);
        assertNotNull(addedFilm.getId());
        assertEquals("Inception", addedFilm.getName());
    }

    @Test
    void testAddFilmWithInvalidReleaseDate() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(1890, 1, 1));
        film.setDuration(148);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals("Дата создания фильма не может быть раньше 28-10-1895: 1890-01-01", exception.getMessage());
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film addedFilm = filmController.addFilm(film);

        addedFilm.setName("Inception new");
        Film updatedFilm = filmController.updateFilm(addedFilm);

        assertEquals("Inception new", updatedFilm.getName());
    }
}