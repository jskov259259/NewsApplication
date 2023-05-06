package ru.clevertec.kalustau.service;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.kalustau.model.JwtAuthentication;
import ru.clevertec.kalustau.model.RoleEnum;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<RoleEnum> getRoles(Claims claims) {
        final List<LinkedHashMap<String, String>> rolesList = claims.get("roles", List.class);
        return rolesList.stream()
                .map(map -> map.get("role"))
                .map(RoleEnum::valueOf)
                .collect(Collectors.toSet());
    }

}

