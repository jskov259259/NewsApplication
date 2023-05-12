package ru.clevertec.kalustau.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Enum representing various roles
 * @author Dzmitry Kalustau
 */
@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {

    ADMIN("ADMIN"),
    JOURNALIST("JOURNALIST"),
    SUBSCRIBER("SUBSCRIBER");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }

}
