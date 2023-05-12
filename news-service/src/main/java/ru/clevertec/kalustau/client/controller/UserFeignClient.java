package ru.clevertec.kalustau.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.kalustau.client.dto.User;

/**
 * Client for sending requests to user-service
 * @author Dzmitry Kalustau
 */
@FeignClient(name = "user-service", url = "${feign.user-client.uri}")
public interface UserFeignClient {

    /**
     * Returns the user by the passed token
     * @param token of authorized user
     * @return response entity with user in body
     */
    @GetMapping("/api/auth/user/{token}")
    ResponseEntity<User> getUserByToken(@PathVariable String token);
}
