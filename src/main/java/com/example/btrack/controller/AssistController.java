package com.example.btrack.controller;

import com.example.btrack.dto.Username;
import com.example.btrack.models.Userdetails;
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

    @PostMapping("/assistants")
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

    // send request for adding assistant
    @PostMapping("/assistants/send")
    public ResponseEntity<Object> addAssistant(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.sendRequest(idToken,user);
        }
    }

    @PostMapping("/assistants/cancel")
    public ResponseEntity<Object> cancelAssistant(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.cancelRequest(idToken,user);
        }
    }

    @PostMapping("/assistants/reject")
    public ResponseEntity<Object> rejectUser(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.rejectRequest(idToken,user);
        }
    }

    @PostMapping("/assistants/accept")
    public ResponseEntity<Object> acceptUser(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.acceptRequest(idToken,user);
        }
    }


    @GetMapping("/assistants/requests")
    public ResponseEntity<Object> getAssistrequests(@RequestHeader("Authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.getMyAssistRequests(idToken);
        }
    }

    // Get my people
    @GetMapping("/assistants/people")
    public ResponseEntity<Object> getPeople(@RequestHeader("Authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.getMyPeople(idToken);
        }
    }

    @GetMapping("/assistants/all")
    public ResponseEntity<Object> getmyAssistants(@RequestHeader("Authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.getMyAssistants(idToken);
        }
    }

    @PostMapping("/assistants/delete")
    public ResponseEntity<Object> deleteAssistant(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.deleteRequest(idToken,user);
        }
    }

    @PostMapping("/assistants/getdetails")
    public ResponseEntity<Object> getDetails(@RequestHeader("Authorization") String authorization, @RequestBody Userdetails user)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return assistService.getDetails(idToken,user);
        }
    }

}
