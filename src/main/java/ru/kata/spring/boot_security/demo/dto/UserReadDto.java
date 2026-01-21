package ru.kata.spring.boot_security.demo.dto;

import java.util.Set;

public class UserReadDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Integer age;
    private Set<RoleDto> roles;

    public UserReadDto() {}

    public UserReadDto(Long id, String username, String name, String surname, Integer age, Set<RoleDto> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Set<RoleDto> getRoles() { return roles; }
    public void setRoles(Set<RoleDto> roles) { this.roles = roles; }
}
