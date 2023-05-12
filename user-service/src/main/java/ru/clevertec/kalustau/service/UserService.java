package ru.clevertec.kalustau.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kalustau.model.User;
import ru.clevertec.kalustau.repository.UserRepository;

import java.util.Optional;

/**
 * Service implementation for managing users.
 * @author Dzmitry Kalustau
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}