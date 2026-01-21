package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.mappers.RoleMapper;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public AdminRestController(UserService userService,
                               RoleService roleService,
                               UserMapper userMapper,
                               RoleMapper roleMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserReadDto>> getUsers() {
        List<UserReadDto> list = userService.getAll().stream()
                .map(userMapper::toReadDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserReadDto> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }
        return ResponseEntity.ok(userMapper.toReadDto(user));
    }

    @GetMapping("/users/form")
    public ResponseEntity<UserFormDto> getCreateForm() {
        List<RoleDto> roles = roleService.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new UserFormDto(null, roles));
    }

    @GetMapping("/users/{id}/form")
    public ResponseEntity<UserFormDto> getEditForm(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }

        List<RoleDto> roles = roleService.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserFormDto(userMapper.toReadDto(user), roles));
    }

    @PostMapping("/users")
    public ResponseEntity<UserReadDto> create(@RequestBody UserWriteDto dto) {
        User created = userService.create(userMapper.toEntity(dto), dto.getRoleIds());
        return ResponseEntity
                .created(URI.create("/api/admin/users/" + created.getId()))
                .body(userMapper.toReadDto(created));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserReadDto> update(@PathVariable Long id, @RequestBody UserWriteDto dto) {
        User updated = userService.update(id, userMapper.toEntity(dto), dto.getRoleIds());
        return ResponseEntity.ok(userMapper.toReadDto(updated));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getRoles() {
        List<RoleDto> roles = roleService.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}
