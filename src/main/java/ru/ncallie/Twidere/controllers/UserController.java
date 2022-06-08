package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ncallie.Twidere.Exception.UserExistsException;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;



    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users/index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public String editPage(@PathVariable("id") User user, Model model) {
        model.addAttribute("allRoles", (Arrays.stream(Role.values()).toList()));
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public String update(@ModelAttribute("user")User user, Model model) {
        try {
            userService.update(user);
        }catch (UserExistsException e) {
            model.addAttribute("allRoles", (Arrays.stream(Role.values()).toList()));
            model.addAttribute("exc", e.getMessage());
            return "users/edit";
        }
        return "redirect:/users";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getOne(user.getId()));
        return "users/profile";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User userForm,
                                @AuthenticationPrincipal User authUser, Model model) {
        try {
            userService.updateProfile(userForm, authUser);
        } catch (UserExistsException e) {
            model.addAttribute("exc", e.getMessage());
            return "users/profile";
        }
        return "redirect:/users/profile";
    }
}
