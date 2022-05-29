package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

@Controller
@RequestMapping("/users")
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
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user")User user) {
        userService.update(user);
        return "redirect:/users";
    }
}
