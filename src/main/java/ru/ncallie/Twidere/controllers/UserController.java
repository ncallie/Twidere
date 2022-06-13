package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ncallie.Twidere.Exception.UserException;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.UserService;

import javax.validation.Valid;
import java.util.Arrays;

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
    public String editPage(@PathVariable("id")  User user, Model model) {
        model.addAttribute("allRoles", (Arrays.stream(Role.values()).toList()));
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", (Arrays.stream(Role.values()).toList()));
            return "users/edit";
        }

        try {
            userService.update(user);
        } catch (UserException e) {
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
    public String updateProfile(@ModelAttribute("user") @Valid User userForm, BindingResult bindingResult,
                                @AuthenticationPrincipal User authUser, Model model) {
        if (bindingResult.hasErrors())
            return "users/profile";
        try {
            userService.updateProfile(userForm, authUser);
        } catch (UserException e) {
            model.addAttribute("exc", e.getMessage());
            return "users/profile";
        }
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile/{username}")
    public String publicPageProfile(@PathVariable("username") String username, Model model, @AuthenticationPrincipal User authUser) {
        User oneByUsername = userService.getOneByUsername(username);
        model.addAttribute("messages", oneByUsername.getMessages());
        model.addAttribute("user", oneByUsername);
        model.addAttribute("isSubscriber", oneByUsername.getSubscribers().contains(authUser));
        model.addAttribute("isAnotherPage", (!username.equals(authUser.getUsername())));
        return "users/publicProfile";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile/{username}/sub")
    public String subscribe(@PathVariable("username") String username, @AuthenticationPrincipal User authUser) {
        userService.subscribe(authUser, username);
        return "redirect:/users/profile/" + username;
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile/{username}/unsub")
    public String unsubscribe(@PathVariable("username") String username, @AuthenticationPrincipal User authUser) {
        userService.unsubscribe(authUser, username);
        return "redirect:/users/profile/" + username;
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile/{username}/subscribers")
    public String subscribersPage(@PathVariable("username") String username, Model model) {
        User oneByUsername = userService.getOneByUsername(username);
        model.addAttribute("subs", oneByUsername.getSubscribers());
        model.addAttribute("name", "Подписчики, " + username);
        return "users/subList";
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/profile/{username}/subscriptions")
    public String subscriptionsPage(@PathVariable("username") String username, Model model) {
        User oneByUsername = userService.getOneByUsername(username);
        model.addAttribute("subs", oneByUsername.getSubscriptions());
        model.addAttribute("name", "Подписки, " + username);
        return "users/subList";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
