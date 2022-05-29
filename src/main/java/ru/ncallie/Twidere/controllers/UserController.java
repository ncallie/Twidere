package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users/index";
    }

    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") User user, Model model) {
        model.addAttribute("allRoles", (Arrays.stream(Role.values()).toList()));
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user")User user) {
        userService.update(user);
        return "redirect:/users";
    }
}
