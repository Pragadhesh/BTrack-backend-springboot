package com.example.btrack.service;

import com.example.btrack.models.User;
import com.example.btrack.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> addUser(String idToken) {
        try {
            // Parse the JWT token to get the username and email
            JWT jwt = new JWT();
            Map<String, Claim> claims = jwt.decodeJwt(idToken).getClaims();

            // Get the "exp" claim from the claims map

            Claim username = claims.get("cognito:username");
            Claim email = claims.get("email");

            // Create a new user entity and save it to the repository
            User user = new User();
            user.setUsername(username.asString());
            user.setEmail(email.asString());
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }
}

