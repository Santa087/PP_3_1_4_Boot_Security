package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class UserWriteDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private Integer age;
    private List<Long> roleIds;

    public UserWriteDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}
