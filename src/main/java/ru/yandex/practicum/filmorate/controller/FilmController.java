package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрошены все фильмы");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
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
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String errorMessage = "Название фильма не может быть пустым";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (film.getDescription().length() > 200) {
            String errorMessage = "Длина описания фильма больше 200 символов: " + film.getDescription();
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 10, 28))) {
            String errorMessage = "Дата создания фильма не может быть раньше 28-10-1895: " + film.getReleaseDate();
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        if (film.getDuration() < 0) {
            String errorMessage = "Продолжительность фильма должна быть положительным числом: " + film.getDuration();
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