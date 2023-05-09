package ru.clevertec.kalustau.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.kalustau.client.dto.User;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserFeignClient {

    @GetMapping("/api/auth/byToken/{token}")
    ResponseEntity<User> getUserByToken(@PathVariable String token);
}
