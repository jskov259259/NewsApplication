package ru.clevertec.kalustau.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO entity representing the user in request
 * @author Dzmitry Kalustau
 */
@Setter
@Getter
public class JwtRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Role> roles;

}
