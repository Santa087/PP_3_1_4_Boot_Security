package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleService.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleService.save(new Role("ROLE_ADMIN")));
        Role userRole = roleService.findByName("ROLE_USER")
                .orElseGet(() -> roleService.save(new Role("ROLE_USER")));

        // админ
        if (userService.findByUsername("admin@mail.ru").isEmpty()) {
            UserWriteDto admin = new UserWriteDto();
            admin.setName("Admin");
            admin.setSurname("Adminov");
            admin.setAge(30);
            admin.setUsername("admin@mail.ru");
            admin.setPassword("admin"); // сервис сам закодирует
            admin.setRoleIds(List.of(adminRole.getId(), userRole.getId()));

            userService.create(admin);
        }
        // юзер
        if (userService.findByUsername("user@mail.ru").isEmpty()) {
            UserWriteDto user = new UserWriteDto();
            user.setName("User");
            user.setSurname("Userov");
            user.setAge(25);
            user.setUsername("user@mail.ru");
            user.setPassword("user");
            user.setRoleIds(List.of(userRole.getId()));

            userService.create(user);
        }
    }
}
