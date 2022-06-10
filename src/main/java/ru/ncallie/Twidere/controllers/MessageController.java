package ru.ncallie.Twidere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ncallie.Twidere.models.Message;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.services.MessageService;
import ru.ncallie.Twidere.services.UserService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model, @ModelAttribute("message")Message message,
                        @RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
        List<Message> all = messageService.getAll();
        if (!all.isEmpty())
            Collections.reverse(all);
        if (filter.isEmpty())
            model.addAttribute("messages", all);
        else
            model.addAttribute("messages", messageService.filter(filter));
        return "messages/index";
    }

    @PostMapping()
    public String createMessage(@ModelAttribute("message") @Valid Message message, BindingResult bindingResult,
                                @AuthenticationPrincipal User user,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (bindingResult.hasErrors())
            return "redirect:/messages";

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

    @DeleteMapping("/{id}")
    public String delMessage(@PathVariable("id") Integer id) {
        messageService.delete(id);
        return "redirect:/messages";
    }
}
