package ru.ncallie.Twidere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ncallie.Twidere.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
