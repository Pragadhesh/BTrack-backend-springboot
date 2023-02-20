package com.example.btrack.controller;

import com.example.btrack.dto.Username;
import com.example.btrack.service.AssistService;
import com.example.btrack.service.Authenticator;
import com.example.btrack.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AssistController {
    @Autowired
    AssistService assistService;

    @Autowired
    Authenticator authService;

    @GetMapping("/assistants")
    public ResponseEntity<Object> getAssistants(@RequestHeader("Authorization") String authorization, @RequestBody Username name)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (name.getUsername() == null || name.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be empty");
        }
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.getAssistants(idToken,name.getUsername());
        }
    }

}
