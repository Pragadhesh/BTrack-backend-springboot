package com.example.btrack.controller;

import com.example.btrack.dto.Note;
import com.example.btrack.dto.Product;
import com.example.btrack.service.Authenticator;
import com.example.btrack.service.NotesService;
import com.example.btrack.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class NotesController {

    @Autowired
    NotesService notesService;

    @Autowired
    Authenticator authService;

    @GetMapping("/notes")
    public ResponseEntity<Object> getNotes(@RequestHeader("authorization") String authorization,@RequestHeader Map<String, String> headers)
    {
        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value));
        });
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return notesService.getNotes(idToken);
        }
    }


    @DeleteMapping("/notes")
    public ResponseEntity<Object> deleteNotes(@RequestHeader("authorization") String authorization,
                                              @RequestBody Note note)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return notesService.deleteNote(idToken,note);
        }
    }

    @PutMapping("/notes")
    public ResponseEntity<Object> updateNotes(@RequestHeader("authorization") String authorization,
                                              @RequestBody Note note)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return notesService.updateNote(idToken,note);
        }
    }

    @PostMapping("/notes")
    public ResponseEntity<Object> addNotes(@RequestHeader("authorization") String authorization,
                                           @RequestBody Note note)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return notesService.addNotes(idToken,note);
        }
    }


}
