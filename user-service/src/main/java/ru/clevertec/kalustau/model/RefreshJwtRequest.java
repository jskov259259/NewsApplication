package ru.clevertec.kalustau.model;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO entity representing token in request
 * @author Dzmitry Kalustau
 */
@Getter
@Setter
public class RefreshJwtRequest {

    public String refreshToken;

}
