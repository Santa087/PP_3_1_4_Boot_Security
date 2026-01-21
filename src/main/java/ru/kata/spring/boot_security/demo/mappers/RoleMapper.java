package ru.kata.spring.boot_security.demo.mappers;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.models.Role;

@Component
public class RoleMapper {
    public RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}
