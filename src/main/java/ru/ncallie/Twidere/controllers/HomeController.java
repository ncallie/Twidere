package ru.ncallie.Twidere.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ncallie.Twidere.models.User;


@Controller
public class HomeController {
    @GetMapping()
    public String index(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "home/index";
    }
}
