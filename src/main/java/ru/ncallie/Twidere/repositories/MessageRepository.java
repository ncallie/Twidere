package ru.ncallie.Twidere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ncallie.Twidere.models.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByTag(String tag);
}
