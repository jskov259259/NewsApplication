package ru.clevertec.kalustau.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kalustau.client.controller.UserFeignClient;
import ru.clevertec.kalustau.client.dto.User;

/**
 * Utility class providing methods for working with the user
 * @author Dzmitry Kalustau
 */
@Service
@RequiredArgsConstructor
public class UserUtility {

    private final UserFeignClient userFeignClient;

    public User getUserByToken(String token) {
        return userFeignClient.getUserByToken(token).getBody();
    }

    public static boolean isUserAdmin(User user) {
        return user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getRole().getAuthority()));
    }

    public static boolean isUserJournalist(User user) {
        return user.getRoles().stream().anyMatch(role -> "JOURNALIST".equals(role.getRole().getAuthority()));
    }

    public static boolean isUserSubscriber(User user) {
        return user.getRoles().stream().anyMatch(role -> "SUBSCRIBER".equals(role.getRole().getAuthority()));
    }

    public static boolean isUserHasRightsToModification(User user, String userName) {
        if (isUserAdmin(user)) {
            return true;
        }
        return user.getUsername().equals(userName);
    }

}

