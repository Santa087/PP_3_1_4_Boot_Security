package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserRestController {

    private final UserService userService;

    public AdminUserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserReadDto> findAll() {
        return userService.getAll().stream()
                .map(this::toReadDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable Long id) {
        User u = userService.getById(id);
        if (u == null) {
            throw new IllegalArgumentException("User not found: id=" + id);
        }
        return toReadDto(u);
    }


    @PostMapping
    public ResponseEntity<UserReadDto> create(@RequestBody UserWriteDto dto) {
        User u = toEntity(dto);
        userService.create(u, dto.getRoleIds());

        User created = userService.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalStateException("Created user not found by username"));

        return ResponseEntity
                .created(URI.create("/api/admin/users/" + created.getId()))
                .body(toReadDto(created));
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable Long id, @RequestBody UserWriteDto dto) {
        User u = toEntity(dto);
        u.setId(id);

        userService.update(u, dto.getRoleIds());
        return toReadDto(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserReadDto toReadDto(User u) {
        Set<RoleDto> roles = u.getRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .collect(Collectors.toSet());

        return new UserReadDto(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getSurname(),
                u.getAge(),
                roles
        );
    }

    private User toEntity(UserWriteDto dto) {
        User u = new User();
        u.setId(dto.getId());
        u.setUsername(dto.getUsername());
        u.setPassword(dto.getPassword());
        u.setName(dto.getName());
        u.setSurname(dto.getSurname());
        u.setAge(dto.getAge());
        return u;
    }
}
