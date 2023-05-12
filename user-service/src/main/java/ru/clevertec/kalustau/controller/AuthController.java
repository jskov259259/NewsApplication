package ru.clevertec.kalustau.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.model.JwtRequest;
import ru.clevertec.kalustau.model.JwtResponse;
import ru.clevertec.kalustau.model.RefreshJwtRequest;
import ru.clevertec.kalustau.model.User;
import ru.clevertec.kalustau.service.AuthService;

/**
 * REST controller for authentication operations.
 * @author Dzmitry Kalustau
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * API Point for login user in system.
     * @param authRequest parameter with user credentials
     * @return JwtResponse with access and refresh tokens
     */
    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * API Point for register user in system.
     * @param authRequest parameter with user credentials
     * @return JwtResponse with access and refresh tokens
     */
    @PostMapping("register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest authRequest) {
        JwtResponse token = authService.register(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * API Point for returning a user by a token
     * @param token token of authorized user
     * @return authorized user
     */
    @GetMapping("user/{token}")
    public ResponseEntity<User> getUserByToken(@PathVariable String token) {
        User user = authService.getUserByToken(token);
        return ResponseEntity.ok(user);
    }

    /**
     * API Point for generate new access token.
     * @param request parameter with refresh token
     * @return JwtResponse with new access token
     */
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    /**
     * API Point for generate new refresh token.
     * @param request parameter with refresh token
     * @return JwtResponse with new refresh and access tokens
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
