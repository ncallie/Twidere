package ru.ncallie.Twidere.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.repositories.MessageRepository;
import ru.ncallie.Twidere.models.Message;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

   private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> filter(String tag) {
        return messageRepository.findByTag(tag);
    }

    public void delete(Integer id, User user) {
        Message message = messageRepository.findById(id).get();
        if (message.getAuthor().getId().equals(user.getId()) || user.getRoles().contains(Role.ADMIN))
            messageRepository.deleteById(id);
    }
}
