package ru.ncallie.Twidere.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ncallie.Twidere.Exception.UserExistsException;
import ru.ncallie.Twidere.models.Role;
import ru.ncallie.Twidere.models.User;
import ru.ncallie.Twidere.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getOne(Integer id) {
        return userRepository.findById(id).get();
    }

    public void save(User user) {
        if (user != null) {
            if (!user.getUsername().isEmpty()) {
                if (userRepository.findByUsername(user.getUsername()) == null) {
                    user.setActive(true);
                    user.setRoles(Collections.singleton(Role.USER));
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                    return;
                }
            }
        }
        throw new UserExistsException("Username already exists!");
    }

    public User getOneByUsername(String username) {
        User byUsername = userRepository.findByUsername(username);
        return byUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void update(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB == null || userFromDB.getId().equals(user.getId()))
            user.setActive(userFromDB == null ? true : userFromDB.isActive());
        else
            throw new UserExistsException("Username already exists!");

        if (user.getEmail().isEmpty())
            user.setEmail(null);
        else {
            userFromDB = userRepository.findByEmail(user.getEmail());
            if (userFromDB != null && !userFromDB.getId().equals(user.getId()))
                throw new UserExistsException("Email already exists!");
        }

        userRepository.save(user);
    }

    public void updateProfile(User userForm, User authUser) {
        User userFromDB = null;
        if (!userForm.getEmail().isEmpty() &&
                userForm.getEmail() != null &&
                !userForm.getPassword().isEmpty() &&
                userForm.getPassword() != null)
            userFromDB = userRepository.findByEmail(userForm.getEmail());
        else
            throw new UserExistsException("Error!");

        if (userFromDB == null || userFromDB.getId().equals(authUser.getId())) {
            authUser.setEmail(userForm.getEmail());
            if (!authUser.getPassword().equals(userForm.getPassword()))
                authUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
            userRepository.save(authUser);
        }
        else
            throw new UserExistsException("Email already exists!");
    }

    public void subscribe(User authUser, String usernameProfile) {
        User byUsername = userRepository.findByUsername(usernameProfile);
        if (!authUser.equals(byUsername)) {
            byUsername.getSubscribers().add(authUser);
            userRepository.save(byUsername);
        }
    }

    public void unsubscribe(User authUser, String usernameProfile) {
        User byUsername = userRepository.findByUsername(usernameProfile);
        if (!authUser.equals(byUsername)) {
            byUsername.getSubscribers().remove(authUser);
            userRepository.save(byUsername);
        }
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
