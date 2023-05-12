package ru.clevertec.kalustau.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An entity representing a user's role
 * @author Dzmitry Kalustau
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;

    private RoleEnum role;

}
