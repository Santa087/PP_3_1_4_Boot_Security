package ru.kata.spring.boot_security.demo.mappers;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserReadDto toReadDto(User u) {
        return new UserReadDto(
                u.getId(),
                u.getUsername(),
                u.getName(),
                u.getSurname(),
                u.getAge(),
                u.getRoles().stream()
                        .map(roleMapper::toDto)
                        .collect(Collectors.toSet())
        );
    }

    public User toEntity(UserWriteDto dto) {
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
