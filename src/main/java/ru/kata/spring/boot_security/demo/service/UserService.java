package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    User getById(Long id);

    User create(User user, java.util.List<Long> roleIds);
    User update(Long id, User user, java.util.List<Long> roleIds);

    void delete(Long id);
    Optional<User> findByUsername(String username);
}

