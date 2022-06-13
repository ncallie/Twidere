package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ncallie.Twidere.Exception.UserException;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class AuthController {
    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("isAnonymous()")
    public String registration(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "auth/registration";
        try {
            userService.save(user);
        } catch (UserException e) {
            model.addAttribute("exc", e.getMessage());
            return "auth/registration";
        }
        return "auth/login";
    }
}
