package ru.clevertec.kalustau.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO entity representing tokens in response
 * @author Dzmitry Kalustau
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}
