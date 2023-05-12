package ru.clevertec.kalustau.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO entity representing the user in the database
 * @author Dzmitry Kalustau
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private Set<Role> roles;

}
