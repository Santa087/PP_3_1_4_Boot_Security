package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class UserFormDto {
    private UserReadDto user;     // может быть null для "new"
    private List<RoleDto> roles;  // все роли из базы

    public UserFormDto() {}

    public UserFormDto(UserReadDto user, List<RoleDto> roles) {
        this.user = user;
        this.roles = roles;
    }

    public UserReadDto getUser() { return user; }
    public void setUser(UserReadDto user) { this.user = user; }

    public List<RoleDto> getRoles() { return roles; }
    public void setRoles(List<RoleDto> roles) { this.roles = roles; }
}
