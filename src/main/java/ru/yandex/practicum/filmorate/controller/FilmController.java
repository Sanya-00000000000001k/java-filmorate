package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.info("Запрошены все фильмы");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        validateFilm(film);
        if (film.getId() == null) {
            String errorMessage = "Требуется указать id фильма";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (!films.containsKey(film.getId())) {
            String errorMessage = "Фильм с указанным ID не найден: " + film.getId();
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 10, 28))) {
            String errorMessage = "Дата создания фильма не может быть раньше 28-10-1895: " + film.getReleaseDate();
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}