package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserReadDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllDtos());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserReadDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDtoById(id));
    }

    @GetMapping("/users/form")
    public ResponseEntity<UserFormDto> getCreateForm() {
        return ResponseEntity.ok(userService.getCreateForm());
    }

    @GetMapping("/users/{id}/form")
    public ResponseEntity<UserFormDto> getEditForm(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getEditForm(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserReadDto> create(@RequestBody UserWriteDto dto) {
        UserReadDto created = userService.create(dto);
        return ResponseEntity
                .created(java.net.URI.create("/api/admin/users/" + created.getId()))
                .body(created);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserReadDto> update(@PathVariable Long id, @RequestBody UserWriteDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(userService.getAllRoleDtos());
    }
}
