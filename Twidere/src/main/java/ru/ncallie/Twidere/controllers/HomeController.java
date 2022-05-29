package ru.ncallie.Twidere.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.ncallie.Twidere.models.Message;

@Controller
public class HomeController {
    @GetMapping()
    public String index() {
        return "home/index";
    }
}
