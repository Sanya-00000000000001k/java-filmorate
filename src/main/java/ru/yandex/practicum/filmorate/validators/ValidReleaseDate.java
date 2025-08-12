package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReleaseDate {
    String message() default "Дата создания фильма не может быть раньше 28-10-1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}