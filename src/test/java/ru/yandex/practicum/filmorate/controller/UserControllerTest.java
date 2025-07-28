package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testFindAllUsers() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userController.createUser(user);

        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
        assertEquals("testLogin", users.iterator().next().getLogin());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);
        assertNotNull(createdUser.getId());
        assertEquals("testLogin", createdUser.getLogin());
    }

    @Test
    void testCreateUserWithEmptyName() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);
        assertEquals("testLogin", createdUser.getName());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        createdUser.setEmail("updated@example.com");
        User updatedUser = userController.updateUser(createdUser);

        assertEquals("updated@example.com", updatedUser.getEmail());
    }
}