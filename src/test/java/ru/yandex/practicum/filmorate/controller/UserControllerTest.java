package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void testFindAll_EmptyList() {
        Collection<User> users = userController.findAll();
        assertTrue(users.isEmpty(), "The list of users should be empty initially.");
    }

    @Test
    void testCreateUser_ValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser.getId(), "The user should have been assigned an ID.");
        assertEquals(1, userController.findAll().size());
        assertEquals("Test Name", createdUser.getName());
    }

    @Test
    void testCreateUser_NameNotProvided_UsesLoginAsName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertEquals("testLogin", createdUser.getName());
    }

    @Test
    void testCreateUser_InvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertTrue(exception.getMessage().contains("Электронная почта не может быть пустой и должна содержать символ '@'"));
    }

    @Test
    void testCreateUser_InvalidLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("test login");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));
    }

    @Test
    void testCreateUser_FutureBirthday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем"));
    }

    @Test
    void testUpdateUser_ValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        createdUser.setName("Updated Name");
        User updatedUser = userController.updateUser(createdUser);

        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void testUpdateUser_NonExistentId() {
        User user = new User();
        user.setId(999L);
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.updateUser(user));
        assertTrue(exception.getMessage().contains("Пользователь с указанным ID не найден"));
    }

    @Test
    void testUpdateUser_NameNotProvided_UsesLoginAsName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        createdUser.setName(null);
        User updatedUser = userController.updateUser(createdUser);

        assertEquals("testLogin", updatedUser.getName());
    }
}