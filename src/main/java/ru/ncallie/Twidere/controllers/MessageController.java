package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ncallie.Twidere.models.Message;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

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
    public String createMessage(@ModelAttribute("message")Message message,
                                @AuthenticationPrincipal User user,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (!uploadPath.isEmpty() && file != null) {
            if (!file.getOriginalFilename().isEmpty()) {
                File upDir = new File(uploadPath);
                if (!upDir.exists())
                    upDir.mkdir();
                String uuidFile = UUID.randomUUID().toString();
                String resultFineName = uuidFile + "." + file.getOriginalFilename();
                message.setFilename(resultFineName);
                file.transferTo(new File(uploadPath + "/" + resultFineName));
            }
        }
        message.setAuthor(user);
        messageService.save(message);
        return "redirect:/messages";
    }
}
