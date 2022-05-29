package ru.ncallie.Twidere.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ncallie.Twidere.Exception.UserExistsException;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) throws UserExistsException {
        if (user != null) {
            if (!user.getUsername().isEmpty()) {
                if (userRepository.findByUsername(user.getUsername()) == null) {
                    user.setActive(true);
                    user.setRoles(Collections.singleton(Role.USER));
                    userRepository.save(user);
                    return;
                }
            }
        }
        throw new UserExistsException("Username already exists!");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void update(User user) {
        userRepository.save(user);
    }
}
