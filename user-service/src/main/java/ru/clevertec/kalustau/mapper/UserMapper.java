package ru.clevertec.kalustau.mapper;

import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.model.JwtRequest;
import ru.clevertec.kalustau.model.User;

@Component
public class UserMapper {

    public User requestToUser(JwtRequest jwtRequest) {
       return User.builder().username(jwtRequest.getUsername())
                .password(jwtRequest.getPassword())
                .firstName(jwtRequest.getFirstName())
                .lastName(jwtRequest.getLastName())
                .roles(jwtRequest.getRoles())
                .build();
    }

}
