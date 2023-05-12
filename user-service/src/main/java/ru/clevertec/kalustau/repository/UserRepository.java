package ru.clevertec.kalustau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.kalustau.model.User;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides methods for interacting with the database
 * @author Dzmitry Kalustau
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns the user by username
     * @param username username of the user.
     * @return a user from the database represented as an Optional object
     */
    Optional<User> findByUsername(String username);

}
