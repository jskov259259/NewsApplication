package ru.clevertec.kalustau.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kalustau.model.JwtAuthentication;
import ru.clevertec.kalustau.service.AuthService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class Controller {

    private final AuthService authService;

    @PreAuthorize("hasAuthority('JOURNALIST')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return ResponseEntity.ok("Hello user " + authInfo.getPrincipal() + "!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        System.out.println(authInfo.getFirstName());
        System.out.println(authInfo.getName());
        System.out.println(authInfo.getUsername());
        System.out.println(authInfo.getRoles());
        return ResponseEntity.ok("Hello admin " + authInfo.getPrincipal() + "!");
    }

}