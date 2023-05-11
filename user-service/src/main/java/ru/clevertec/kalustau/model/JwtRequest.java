package ru.clevertec.kalustau.model;

import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Setter
@Getter
public class JwtRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Role> roles;

}
