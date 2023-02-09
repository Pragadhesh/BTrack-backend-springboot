package com.example.btrack.repository;



import com.example.btrack.models.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Userdetails, Long> {
    Optional<Userdetails> findByUsername(String username);
}

