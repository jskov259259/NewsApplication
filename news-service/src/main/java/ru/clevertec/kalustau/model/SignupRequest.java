package ru.clevertec.kalustau.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    private String username;

    private String password;

    private String email;

    Set<String> role = new HashSet<>();
}
