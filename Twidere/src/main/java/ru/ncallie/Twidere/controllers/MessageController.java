package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ncallie.Twidere.models.Message;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.MessageService;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping()
    public String index(Model model, @ModelAttribute("message")Message message,
                        @RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
        if (filter.isEmpty())
            model.addAttribute("messages", messageService.getAll());
        else
            model.addAttribute("messages", messageService.filter(filter));
        return "messages/index";
    }

    @PostMapping()
    public String createMessage(@ModelAttribute("message")Message message, @AuthenticationPrincipal User user) {
        System.out.println(message.getText());
        message.setAuthor(user);
        messageService.save(message);
        return "redirect:/messages";
    }
}
