package com.example.btrack.controller;

import com.example.btrack.service.Authenticator;
import com.example.btrack.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    Authenticator authService;

    @Autowired
    UserService userService;



    @PostMapping("/user/add")
    public ResponseEntity<Object> validateUser(@RequestHeader("authorization") String authorization)
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

    @PostMapping("/user/refresh")
    public ResponseEntity<String> getUpdatedTokens (@RequestHeader("authorization") String authorization )
    {
        String refreshToken = authorization.replace("Bearer ", "");
        return authService.refreshUserToken(refreshToken);
    }

}
