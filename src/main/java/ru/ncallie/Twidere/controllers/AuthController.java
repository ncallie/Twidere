package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ncallie.Twidere.Exception.UserExistsException;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

@Controller
@RequestMapping("/registration")
public class AuthController {
    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String registration(@ModelAttribute("user")User user) {
        return "auth/registration";
    }

    @PostMapping()
    public String create(@ModelAttribute("user")User user, Model model) {
        try {
            userService.save(user);
        } catch (UserExistsException e) {
            model.addAttribute("exc", e.getMessage());
            return "auth/registration";
        }
        return "auth/login";
    }
}
