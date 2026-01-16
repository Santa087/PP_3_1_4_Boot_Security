package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("admin/index");
        mav.addObject("users", userService.getAll());
        mav.addObject("allRoles", roleService.findAll());
        mav.addObject("user", new User());
        return mav;
    }


    @GetMapping("/new")
    public ModelAndView newUser() {
        ModelAndView mav = new ModelAndView("admin/new");
        mav.addObject("user", new User());
        mav.addObject("allRoles", roleService.findAll());
        return mav;
    }

    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute("user") User user,
                               @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.create(user, roleIds);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam("id") Long id) {
        ModelAndView mav = new ModelAndView("admin/edit");
        mav.addObject("user", userService.getById(id));
        mav.addObject("allRoles", roleService.findAll());
        return mav;
    }

    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute("user") User user,
                               @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        userService.update(user, roleIds);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return new ModelAndView("redirect:/admin");
    }
}
