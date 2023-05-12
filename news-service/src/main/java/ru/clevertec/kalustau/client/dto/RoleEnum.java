package ru.clevertec.kalustau.client.dto;

import lombok.RequiredArgsConstructor;

/**
 * Enum representing various roles
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
public enum RoleEnum {

    ADMIN("ADMIN"),
    JOURNALIST("JOURNALIST"),
    SUBSCRIBER("SUBSCRIBER");

    private final String value;

    public String getAuthority() {
        return value;
    }

}
