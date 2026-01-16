package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
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

        userService.findByUsername("admin").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin"); // НЕ кодируем тут
            admin.setName("Admin");
            admin.setSurname("Root");
            admin.setAge(30);

            userService.create(admin, List.of(adminRole.getId(), userRole.getId()));
            return admin;
        });

        userService.findByUsername("user").orElseGet(() -> {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user"); // НЕ кодируем тут
            user.setName("User");
            user.setSurname("Simple");
            user.setAge(25);

            userService.create(user, List.of(userRole.getId()));
            return user;
        });
    }
}
