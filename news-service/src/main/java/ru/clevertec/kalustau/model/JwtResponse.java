package ru.clevertec.kalustau.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String jwt;

    private Long id;

    private String username;

    private String email;

    private List<String> roles = new ArrayList<>();
}
