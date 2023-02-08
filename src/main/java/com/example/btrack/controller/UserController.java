package com.example.btrack.controller;

import com.example.btrack.service.Authenticator;
import com.example.btrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    Authenticator authService;

    @Autowired
    UserService userService;

    @GetMapping("/user/add")
    public ResponseEntity<Object> validateUser(@RequestHeader("Authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);

        }
        else
        {
            return userService.addUser(idToken);
        }
    }

    @GetMapping("/user/refresh")
    public ResponseEntity<String> getUpdatedTokens (@RequestHeader("Authorization") String authorization )
    {
        String refreshToken = authorization.replace("Bearer ", "");
        return authService.refreshUserToken(refreshToken);
    }

}
