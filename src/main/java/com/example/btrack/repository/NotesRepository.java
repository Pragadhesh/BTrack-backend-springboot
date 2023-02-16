package com.example.btrack.repository;

import com.example.btrack.models.Notes;
import com.example.btrack.models.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByUser(Userdetails actualUser);
}


