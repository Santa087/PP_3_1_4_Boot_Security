package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/index";
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found: " + principal.getName()));
        model.addAttribute("user", user);
        return "user/index";
    }

}
