package com.example.btrack.service;

import com.example.btrack.dto.Note;
import com.example.btrack.dto.Product;
import com.example.btrack.models.Notes;
import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotesService {

    @Autowired
    UserService userService;

    @Autowired
    NotesRepository notesRepository;

    public ResponseEntity<Object> addNotes(String idToken, Note note) {
        System.out.println("Entered addNotes");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                if(note.getTitle()!= null || note.getDescription()!= null) {
                    Notes notes = new Notes(note.getTitle(), note.getDescription(), actualUser);
                    Notes nd = notesRepository.save(notes);
                    List<Notes> result = notesRepository.findByUser(actualUser);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
                else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getNotes(String idToken) {
        System.out.println("Entered getNotes");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Notes> result = notesRepository.findByUser(actualUser);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Object> deleteNote(String idToken, Note note) {
        System.out.println("Entered deleteNote");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Notes> pr = notesRepository.findById(note.getId());
                Notes products = pr.orElse(null);
                if (products == null || products.getUser().getUsername() != actualUser.getUsername()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                } else {
                    notesRepository.deleteById(products.getId());
                    List<Notes> result = notesRepository.findByUser(actualUser);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<Object> updateNote(String idToken, Note note) {
        System.out.println("Entered updateNote");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Notes> pr = notesRepository.findById(note.getId());
                Notes products = pr.orElse(null);
                if (products == null || products.getUser().getUsername() != actualUser.getUsername()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                } else {
                    if(note.getTitle()!= null || note.getDescription()!= null) {
                        products.setTitle(note.getTitle());
                        products.setDescription(note.getDescription());
                        notesRepository.save(products);
                        List<Notes> result = notesRepository.findByUser(actualUser);
                        return new ResponseEntity<>(result, HttpStatus.OK);
                    }
                    else
                    {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
