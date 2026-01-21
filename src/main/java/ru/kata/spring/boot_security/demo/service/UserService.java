package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    List<UserReadDto> getAllDtos();

    UserReadDto getDtoById(Long id);

    UserFormDto getCreateForm();
    UserFormDto getEditForm(Long id);

    UserReadDto create(UserWriteDto dto);
    UserReadDto update(Long id, UserWriteDto dto);

    void delete(Long id);

    List<RoleDto> getAllRoleDtos();
}
