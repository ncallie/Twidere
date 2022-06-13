package ru.ncallie.Twidere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.ncallie.Twidere.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
   User findByUsername(String username);

    User findByEmail(String email);
}
